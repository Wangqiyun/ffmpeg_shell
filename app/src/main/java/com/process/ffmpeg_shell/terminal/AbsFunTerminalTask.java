package com.process.ffmpeg_shell.terminal;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.process.ffmpeg_shell.common.AssetFileStorageHandler;
import com.process.ffmpeg_shell.log.Logger;
import com.process.ffmpeg_shell.utils.FileStorageDisposeUtils;
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
public abstract class AbsFunTerminalTask implements IInstallFunTerminalTask, AssetFileStorageHandler.OnAssetFileStorageListener {
    private static final String TAG = "AbsFunTerminalTask";

    private WeakReference<OnInstallFunTerminalListener> mListenerWeakReference = null;
    private WeakReference<Context> mContextWeakReference = null;

    private static final String CHMOD_VALUE = "chmod";
    private static final String CHMOD_VALUE_R = "-R";
    private static final String PERMISSION_777 = "777";

    public AbsFunTerminalTask(Context context) {
        mContextWeakReference = new WeakReference<>(context);
    }

    @Override
    public IInstallFunTerminalTask initialized() {
        AssetFileStorageHandler handler = new AssetFileStorageHandler();
        handler.setOnAssetFileStorageListener(this);

        Context context = obtainContext();
        if(context == null) {
            Logger.w(TAG, "initialized() context is null.");
            return null;
        }

        String cachePath = CacheStorageUtils.obtainExternalCacheDir(context);
        if(TextUtils.isEmpty(cachePath)) {
            Logger.i(TAG, "initialized() cache path not is empty.");
            return null;
        }

        if(mContextWeakReference == null) {
            Logger.w(TAG, "initialized() context reference is null.");
            return null;
        }

        String fileName = obtainAssetsFileName();
        handler.asyncSaveAssetFileToStorage(context, fileName, cachePath);

        return this;
    }

    @Override
    public void release() {
        Logger.i(TAG, "release()");

        mListenerWeakReference = null;
        mContextWeakReference = null;
    }

    @Override
    public IInstallFunTerminalTask setOnInstallFunTerminalListener(OnInstallFunTerminalListener listener) {
        this.mListenerWeakReference = new WeakReference<OnInstallFunTerminalListener>(listener);
        return this;
    }

    @Override
    public void onAssetFileStorageFinish(String path) {
        InstallFunTerminalResult result = new InstallFunTerminalResult();

        if(!TextUtils.isEmpty(path)) {
            result.setCode(InstallFunTerminalResult.CODE_FINISH).setPath(path);
        } else {
            result.setCode(InstallFunTerminalResult.CODE_ERROR);
        }

        handleInstallFunTerminalResult(result);
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
}
