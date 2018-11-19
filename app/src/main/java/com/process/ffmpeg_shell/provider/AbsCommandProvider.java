package com.process.ffmpeg_shell.provider;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.process.ffmpeg_shell.log.Logger;
import com.process.ffmpeg_shell.terminal.IInstallFunTerminalTask;
import com.process.ffmpeg_shell.terminal.ShellCommand;
import com.process.ffmpeg_shell.utils.MainHandlerUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 命令支持抽象类
 * Created by kerwin on 2018/11/15
 */
public abstract class AbsCommandProvider implements
        IInstallFunTerminalTask.OnInstallFunTerminalListener,
        ShellCommand.OnShellCommandProcessingListener {
    private static final String TAG = "AbsCommandProvider";
    private static final String HANDLER_THREAD = "HANDLER_THREAD";

    private static final String COMMAND_KEY = "command_key";
    private static final String COMMAND_SESSION_KEY = "command_session_key";
    private static final String COMMAND_PARAM = "command_param";
    private static final int COMMAND_WHAT = 1001;

    private List<String> mCommandList = null;
    private ShellCommand mShellCommand = null;

    private android.os.Handler mQueueHandler = null;
    private HandlerThread mHandlerThread = null;

    private WeakReference<OnCommandProviderListener> mCommandProviderListenerWeakReference = null;

    private boolean isTerminalSupportExecute = false;
    private String mTerminalExecutePath = "";

    public AbsCommandProvider(Context context) {
        mHandlerThread = new HandlerThread(HANDLER_THREAD);
        mHandlerThread.start();

        mQueueHandler = new CommandHandler(mHandlerThread.getLooper(), this);

        mShellCommand = ShellCommand.builder();
        mShellCommand.setOnShellCommandProcessingListener(this);

        IInstallFunTerminalTask terminalTask = generateInstallFunTerminalTask(context);
        if(terminalTask == null) {
            Logger.w(TAG, "AbsCommandProvider() terminal task object not is null.");
        } else {
            terminalTask.setOnInstallFunTerminalListener(this);
            terminalTask.initialized();
        }

        initialized(context);
    }

    @Override
    public void onInstallFunTerminal(IInstallFunTerminalTask.InstallFunTerminalResult result) {
        if(result == null) {
            Logger.w(TAG, "onInstallFunTerminal() result not is null.");
            return;
        }

        if(result.getCode() == IInstallFunTerminalTask.InstallFunTerminalResult.CODE_FINISH) {
            this.isTerminalSupportExecute = true;
            this.mTerminalExecutePath = result.getPath();
        }
    }

    @Override
    public void onShellCommandProcessing(long session, String line) {
        Logger.w(TAG, line);
    }

    /**
     * 命令支持者释放资源方法
     * */
    public void release() {
        if(mQueueHandler != null) {
            mQueueHandler.removeMessages(COMMAND_WHAT);
            mQueueHandler = null;
        }

        if(mHandlerThread != null) {
            mHandlerThread.quit();
            mHandlerThread = null;
        }
    }

    /**
     * 设置命令支持监听器实体
     * @param listener 监听器实例
     * */
    public void setOnCommandProviderListener(OnCommandProviderListener listener) {
        mCommandProviderListenerWeakReference = new WeakReference<OnCommandProviderListener>(listener);
    }

    /**
     * 通过handler进行异步命令处理，并且是队列形式-逐条处理的
     * @param bundle
     * @param commands 命令行参数列表 */
    protected long queueCommandExecute(Bundle bundle, ArrayList<String> commands) {
        if(mQueueHandler == null) {
            Logger.w(TAG, "queueCommandExecute() handler is null.");
            return 0;
        }

        if(mHandlerThread == null) {
            Logger.w(TAG, "queueCommandExecute() handler thread is null.");
            return 0;
        }

        long session = System.currentTimeMillis();

        Bundle extra = new Bundle();
        extra.putLong(COMMAND_SESSION_KEY, session);
        extra.putStringArrayList(COMMAND_KEY, commands);
        extra.putBundle(COMMAND_PARAM, bundle);

        Message message = new Message();
        message.what = COMMAND_WHAT;
        message.setData(extra);
        mQueueHandler.sendMessage(message);
        return session;
    }

    /**
     * 用于构建执行命令字段列表
     * @param args 命令行的参数数组
     * */
    protected ArrayList<String> buildCommandList(String[] args) {
        if(args == null || args.length == 0) {
            Logger.w(TAG, "buildCommandList() args not is null.");
            return null;
        }

        if(TextUtils.isEmpty(mTerminalExecutePath)) {
            Logger.w(TAG, "buildCommandList() terminal path is empty.");
            return null;
        }

        ArrayList<String> list = new ArrayList<String>();
        list.add(mTerminalExecutePath);
        list.addAll(Arrays.asList(args));

        return list;
    }

    /**
     * 处理执行命令行消息
     * @param message 命令行消息
     * */
    private void handleCommandMessage(Message message) {
        if(message == null) {
            Logger.w(TAG, "handleMessage() message not is null.");
            return;
        }

        Bundle bundle = message.getData();
        ArrayList<String> commandList = bundle.getStringArrayList(COMMAND_KEY);
        long session = bundle.getLong(COMMAND_SESSION_KEY);
        Bundle extra = bundle.getBundle(COMMAND_PARAM);

        if(message.what == COMMAND_WHAT) {
            executeCommand(commandList, extra, session);
        }
    }

    /**
     * 执行命令行处理
     * @param commands 命令行参数列表
     * @param extra 请求参数值
     * @param session 当前执行命令的session
     * */
    private void executeCommand(ArrayList<String> commands, Bundle extra, long session) {
        if(!isTerminalSupportExecute) {
            Logger.w(TAG, "queueCommandExecute() current not support execute.");
            return;
        }

        ProcessBuilder builder = new ProcessBuilder();
        builder.command(commands);

        mShellCommand.setOnShellCommandProcessingListener(this);
        boolean isExecSuccess = mShellCommand.exec(session, builder);

        if(!isExecSuccess) {
            Logger.w(TAG, "executeCommand() command fail.");
        } else {
            notifyCommandProviderFinish(session, extra);
        }
    }

    /**
     * 通知命令行处理完毕时触发回调
     * @param session 命令行会话id值
     * @param extra 请求参数值
     * */
    private void notifyCommandProviderFinish(final long session, final Bundle extra) {
        if(mCommandProviderListenerWeakReference == null) {
            Logger.w(TAG, "notifyCommandProviderFinish() listener reference is null.");
            return;
        }

        final OnCommandProviderListener listener = mCommandProviderListenerWeakReference.get();
        MainHandlerUtils.post(new MainHandlerUtils.Observer() {
            @Override
            public void onObserver() {
                if(listener == null) {
                    Logger.w(TAG, "notifyCommandProviderFinish() listener is null.");
                } else {
                    listener.onCommandProviderFinish(session, extra);
                }
            }
        });
    }

    /**
     * 得到功能终端安装任务的实体
     * @param context 内容上下文对象
     * @return 返回功能终端安装任务对象
     * */
    public abstract IInstallFunTerminalTask generateInstallFunTerminalTask(Context context);

    /**
     * 各模块进行自身的初始化工作
     * @param context 内容上下文
     * */
    protected abstract void initialized(Context context);

    /**
     * 得到当前模块缓存目录(一般为sdcard路径)
     * */
    public abstract String getProviderCacheDir(Context context);

    /**
     * 命令线程处理实现类(保证命令在同一线程下进行,避免异步导致命令不按照队列方式走)
     * */
    private static class CommandHandler extends Handler {
        /** 使用弱引用来处理异步操作问题,避免内存泄露 */
        private WeakReference<AbsCommandProvider> mProviderWeakReference = null;

        public CommandHandler(Looper looper, AbsCommandProvider provider) {
            super(looper);
            this.mProviderWeakReference = new WeakReference<AbsCommandProvider>(provider);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(mProviderWeakReference == null) {
                Logger.w(TAG, "handleMessage() provider reference not is null.");
                return;
            }

            AbsCommandProvider provider = mProviderWeakReference.get();
            if(provider == null) {
                Logger.w(TAG, "handleMessage() provider not is null.");
            } else {
                provider.handleCommandMessage(msg);
            }
        }
    }

    /** 命令支持者监听器 */
    public interface OnCommandProviderListener {
        /**
         * 命令执行完成时触发回调
         * @param session 执行操作的会话值
         * @param extra 请求参数值
         * */
        void onCommandProviderFinish(long session, Bundle extra);

        /**
         * 命令执行执行中触发回调
         * @param session 执行操作的会话值
         * @param result 当前执行中返回结果
         * */
        void onCommandProviderProgress(long session, String result);
    }
}
