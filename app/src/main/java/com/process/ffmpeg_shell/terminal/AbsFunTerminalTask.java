package com.process.ffmpeg_shell.terminal;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.process.ffmpeg_shell.log.Logger;
import com.process.ffmpeg_shell.terminal.common.FunTerminalFileDispose;
import com.process.ffmpeg_shell.terminal.common.ShellCommand;
import com.process.ffmpeg_shell.utils.CacheStorageUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 抽象的二进制终端加载器任务
 * Created by kerwin on 2018/11/13
 */
public abstract class AbsFunTerminalTask implements IInstallFunTerminalTask {
    private static final String TAG = "AbsFunTerminalTask";

    private WeakReference<OnInstallFunTerminalListener> mListenerWeakReference = null;
    private WeakReference<Context> mContextWeakReference = null;
    private FunTerminalFileDispose mFunTerminalFileDispose = null;

    private static final String CHMOD_VALUE = "chmod";
    private static final String CHMOD_VALUE_R = "-R";
    private static final String PERMISSION_777 = "777";

    public AbsFunTerminalTask(Context context) {
        mContextWeakReference = new WeakReference<>(context);
        mFunTerminalFileDispose = new FunTerminalFileDispose();
    }

    @Override
    public IInstallFunTerminalTask initialized() {
        AsyncInstallTask installTask = new AsyncInstallTask(this);
        installTask.execute();
        return this;
    }

    @Override
    public void release() {
        Logger.i(TAG, "release()");

        mListenerWeakReference = null;
        mContextWeakReference = null;
        mFunTerminalFileDispose = null;
    }

    @Override
    public IInstallFunTerminalTask setOnInstallFunTerminalListener(OnInstallFunTerminalListener listener) {
        this.mListenerWeakReference = new WeakReference<OnInstallFunTerminalListener>(listener);
        return this;
    }

    /**
     * 通知调用者执行安装终端结果
     * @param result 安装终端结果值
     * */
    private void notifyInstallTerminalResult(InstallFunTerminalResult result) {
        if (this.mListenerWeakReference == null) {
            Logger.w(TAG, "notifyInstallTerminalResult() listener reference not is null.");
            return;
        }

        OnInstallFunTerminalListener listener = mListenerWeakReference.get();
        if (listener == null) {
            Logger.w(TAG, "notifyInstallTerminalResult() listener not is null.");
            return;
        }

        listener.onInstallFunTerminal(result);
    }

    /**
     * 得到内容上下文对象实体
     * */
    private Context obtainContext() {
        if(mContextWeakReference == null) {
            Logger.w(TAG, "obtainContext() context reference not is null.");
            return null;
        }

        return mContextWeakReference.get();
    }


    /**
     * 保存功能终端(二进制)模块到缓存中
     * */
    private String saveFunTerminalToCache() {
        if(mFunTerminalFileDispose == null) {
            Logger.i(TAG, "saveFunTerminalToCache() file supporter not is null.");
            return "";
        }

        Context context = obtainContext();
        if(context == null) {
            Logger.i(TAG, "saveFunTerminalToCache() context not is null.");
            return "";
        }

        AssetManager manager = context.getAssets();
        if(manager == null) {
            Logger.i(TAG, "saveFunTerminalToCache() manager not is null.");
            return "";
        }

        String cachePath = CacheStorageUtils.obtainExternalCacheDir(context);
        if(TextUtils.isEmpty(cachePath)) {
            Logger.i(TAG, "saveFunTerminalToCache() cache path not is empty.");
            return "";
        }

        try {
            String fileName = obtainAssetsFileName();
            InputStream input = manager.open(fileName);
            String path = cachePath + File.separator + fileName;
            Logger.i(TAG, "saveFunTerminalToCache() fileName:" + fileName + ",path:" + path);

            boolean isSaveSuccess = mFunTerminalFileDispose.saveInputStreamToCache(input, path);
            return isSaveSuccess ? path : "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 授权功能终端(二进制)读写权限(如需要特殊处理-子类可单独实现)
     * */
    protected boolean accreditFunTerminalFile(String path) {
        List<String> commandList = new ArrayList<String>();
        commandList.add(CHMOD_VALUE);
        commandList.add(CHMOD_VALUE_R);
        commandList.add(PERMISSION_777);
        commandList.add(path);

        ProcessBuilder builder = new ProcessBuilder();
        builder.command(commandList);

        ShellCommand command = ShellCommand.builder();
        return command.exec(0, builder);
    }

    /**
     * 处理功能终端(二进制)安装结果处理
     * @param result 功能终端安装结果实体
     * */
    private void handleInstallFunTerminalResult(InstallFunTerminalResult result) {
        if(result == null) {
            Logger.w(TAG, "handleInstallFunTerminalResult() result not is null.");
            return;
        }

        if(result.getCode() == InstallFunTerminalResult.CODE_FINISH) {
            boolean isAccreditSuccess = accreditFunTerminalFile(result.getPath());
            if(!isAccreditSuccess) {
                result.setCode(InstallFunTerminalResult.CODE_ERROR);
            }
        }

        notifyInstallTerminalResult(result);
    }

    /**
     * 获取asset资源文件名称
     * @return 返回当前需要安装加载的资源文件名称
     * */
    protected abstract String obtainAssetsFileName();

    /**
     * 临时使用AsyncTask做异步操作(后续可以使用其它异步工具进行替代)
     * */
    private static class AsyncInstallTask extends AsyncTask<Void, Void, InstallFunTerminalResult> {
        private static final String TAG = "AsyncInstallTask";
        private WeakReference<AbsFunTerminalTask> mFunTerminalTaskWeakReference = null;

        public AsyncInstallTask(AbsFunTerminalTask task) {
            mFunTerminalTaskWeakReference = new WeakReference<AbsFunTerminalTask>(task);
        }

        @Override
        protected InstallFunTerminalResult doInBackground(Void... voids) {
            InstallFunTerminalResult result = new InstallFunTerminalResult();
            String mFilePath = "";

            AbsFunTerminalTask task = obtainFunTerminalTask();
            if(task == null) {
                Logger.w(TAG, "doInBackground() task not is null.");
            } else {
                mFilePath = task.saveFunTerminalToCache();
            }

            if(!TextUtils.isEmpty(mFilePath)) {
                result.setCode(InstallFunTerminalResult.CODE_FINISH).setPath(mFilePath);
            } else {
                result.setCode(InstallFunTerminalResult.CODE_ERROR);
            }

            return result;
        }

        @Override
        protected void onPostExecute(InstallFunTerminalResult result) {
            AbsFunTerminalTask task = obtainFunTerminalTask();
            if(task == null) {
                Logger.w(TAG, "onPostExecute() task not is null.");
                return;
            }

            task.handleInstallFunTerminalResult(result);
        }

        /**
         * 取到终端(二进制)任务实体
         * */
        private AbsFunTerminalTask obtainFunTerminalTask() {
            if(mFunTerminalTaskWeakReference == null) {
                Logger.w(TAG, "obtainFunTerminalTask() reference is null.");
                return null;
            }

            return mFunTerminalTaskWeakReference.get();
        }
    }
}
