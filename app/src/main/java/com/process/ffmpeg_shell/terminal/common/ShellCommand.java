package com.process.ffmpeg_shell.terminal.common;

import com.process.ffmpeg_shell.log.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by kerwin on 2018/11/14
 */
public class ShellCommand {
    private static final String TAG = "ShellCommand";

    private static final long CODE_SUCCESS = 0;
    private static final long NOT_SESSION = -1;

    private boolean isPrintExecCommand = false;
    private WeakReference<OnShellCommandProcessingListener> mShellCommandProcessingWeakReference = null;

    public static ShellCommand builder() {
        return new ShellCommand();
    }

    /**
     * 执行linux shell命令
     * @param session 当前执行命令的会话id
     * @param builder 执行处理命令对象
     * */
    public boolean exec(long session, ProcessBuilder builder) {
        printExecCommand(builder);

        try {
            Process process = builder.redirectErrorStream(true).start();
            process.getOutputStream().close();
            process.getErrorStream().close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";
            while ((line = reader.readLine()) != null) {
                notifyShellCommandProcessing(session, line);
            }

            int code = process.waitFor();
            process.destroy();

            return CODE_SUCCESS == code;
        } catch (Throwable e) {
            Logger.e(TAG, "ERROR", e);
            return false;
        }
    }

    /**
     * 执行linux shell命令
     * @param builder 执行处理命令对象
     * */
    public boolean exec(ProcessBuilder builder) {
        return exec(NOT_SESSION, builder);
    }

    /**
     * 设置是否打印执行命令行log
     * @param isPrintExecCommand 标记是否命令打印
     * */
    public void setPrintExecCommand(boolean isPrintExecCommand) {
        this.isPrintExecCommand = isPrintExecCommand;
    }

    /**
     * 设置shell命令处理中监听器实体
     * @param listener shell命令处理监听器实例
     * */
    public void setOnShellCommandProcessingListener(OnShellCommandProcessingListener listener) {
        this.mShellCommandProcessingWeakReference = new WeakReference<OnShellCommandProcessingListener>(listener);
    }

    /**
     * 通知shell命令处理中时触发回调
     * @param session 命令会话id
     * @param line 执行命令结果行值
     * */
    private void notifyShellCommandProcessing(long session, String line) {
        if(session == NOT_SESSION) {
            Logger.d(TAG, "notifyShellCommandProcessing() current there is no session.");
            return;
        }

        if(mShellCommandProcessingWeakReference == null) {
            Logger.w(TAG, "notifyShellCommandProcessing() reference is null.");
            return;
        }

        OnShellCommandProcessingListener listener = mShellCommandProcessingWeakReference.get();
        if(listener == null) {
            Logger.w(TAG, "notifyShellCommandProcessing() listener is null.");
        } else {
            listener.onShellCommandProcessing(session, line);
        }
    }

    /**
     * 打印执行命令行
     * @param builder 处理命令行
     * */
    private void printExecCommand(ProcessBuilder builder) {
        // 当前不打印执行命令行
        if(!isPrintExecCommand) {
            return;
        }

        StringBuilder log = new StringBuilder();
        List<String> commands = builder.command();
        for(String command : commands) {
            log.append(command).append(" ");
        }

        Logger.i(TAG, "printExecCommand() log > " + log.toString());
    }

    /**
     * 进行shell命令正在处理中监听器
     * */
    public interface OnShellCommandProcessingListener {
        /**
         * 进行shell命令处理中时触发回调
         * @param session 当前执行会话id值
         * @param line 命令执行结果行
         * */
        void onShellCommandProcessing(long session, String line);
    }
}
