package com.process.ffmpeg_shell.common;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.process.ffmpeg_shell.log.Logger;
import com.process.ffmpeg_shell.utils.FileStorageDisposeUtils;

import java.io.File;
import java.io.InputStream;
import java.lang.ref.WeakReference;

/**
 * Created by kerwin on 2018/11/19
 */
public class AssetFileStorageHandler {
    private static final String TAG = "AssetFileStorageHandler";

    private WeakReference<OnAssetFileStorageListener> mOnAssetFileStorageListenerWeakReference;

    public void asyncSaveAssetFileToStorage(Context context, String fileName, String parentPath) {
        AsyncSaveTask task = new AsyncSaveTask(context, fileName, parentPath);
        task.setAssetFileStorageHandler(this);
        task.execute();
    }

    public void setOnAssetFileStorageListener(OnAssetFileStorageListener listener) {
        mOnAssetFileStorageListenerWeakReference = new WeakReference<OnAssetFileStorageListener>(listener);
    }

    /**
     * 通知asset文件存储完成
     * @param path 文件路径
     * */
    private void notifyAssetFileStorageFinish(String path) {
        if(mOnAssetFileStorageListenerWeakReference == null) {
            Logger.w(TAG, "notifyAssetFileStorageFinish() listener wake reference is null.");
            return;
        }

        OnAssetFileStorageListener listener = mOnAssetFileStorageListenerWeakReference.get();
        if(listener == null) {
            Logger.w(TAG, "notifyAssetFileStorageFinish() listener is null.");
        } else {
            listener.onAssetFileStorageFinish(path);
        }
    }

    private static class AsyncSaveTask extends AsyncTask<Void, Void, String> {
        private String mFileName;
        private String mParentPath;
        private WeakReference<Context> mContextWeakReference;
        private WeakReference<AssetFileStorageHandler> mAssetFileStorageHandlerWeakReference;

        public AsyncSaveTask(Context context, String fileName, String parentPath) {
            this.mFileName = fileName;
            this.mParentPath = parentPath;
            this.mContextWeakReference = new WeakReference<Context>(context);
        }

        public void setAssetFileStorageHandler(AssetFileStorageHandler handler) {
            mAssetFileStorageHandlerWeakReference = new WeakReference<AssetFileStorageHandler>(handler);
        }

        @Override
        protected String doInBackground(Void... voids) {
            if(mContextWeakReference == null) {
                Logger.w(TAG, "doInBackground() context weak reference is null.");
                return "";
            }

            Context context = mContextWeakReference.get();
            if(context == null) {
                Logger.w(TAG, "doInBackground() context not is null.");
                return "";
            }

            if(TextUtils.isEmpty(mFileName)) {
                Logger.w(TAG, "doInBackground() file name not is empty.");
                return "";
            }

            if(TextUtils.isEmpty(mParentPath)) {
                Logger.w(TAG, "doInBackground() parent path not is empty.");
                return "";
            }

            try {
                String filePath = this.mParentPath + File.separator + mFileName;
                AssetManager manager = context.getAssets();
                InputStream input = manager.open(mFileName);
                boolean isSaveSuccess = FileStorageDisposeUtils.saveInputStreamToCache(input, filePath);

                Logger.i(TAG, "doInBackground() isSaveSuccess:" + isSaveSuccess + ",filePath:" + filePath);
                return filePath;
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return "";
        }

        @Override
        protected void onPostExecute(String value) {
            if(mAssetFileStorageHandlerWeakReference == null) {
                Logger.w(TAG, "asset file storage handle reference is null.");
                return;
            }

            AssetFileStorageHandler handler = mAssetFileStorageHandlerWeakReference.get();
            handler.notifyAssetFileStorageFinish(value);
        }
    }

    /**
     * asset文件存储监听器
     * */
    public interface OnAssetFileStorageListener {
        /**
         * asset文件存储完成时触发回调
         * @param path 文件路径
         * */
        void onAssetFileStorageFinish(String path);
    }
}
