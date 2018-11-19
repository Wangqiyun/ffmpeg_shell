package com.process.ffmpeg_shell.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.process.ffmpeg_shell.log.Logger;

import java.io.File;

/**
 * 缓存存储支持实现类
 * Created by kerwin on 2018/11/12
 */
public class CacheStorageUtils {
    private static final String TAG = "CacheStorageUtils";

    /**
     * 得到App的缓存目录
     * @param context 内容上下文对象
     * */
    public static String obtainExternalCacheDir(Context context) {
        File file = context.getFilesDir();
        if(file == null) {
            Log.i(TAG, "saveFileToCache() file == null.");
            return "";
        }

        return file.getPath();
    }

    /**
     * 得到sdcard存储目录路径
     * @param context 内容上下文对象
     * */
    public static String obtainExternalStorageDir(Context context) {
        String storageState = Environment.getExternalStorageState();
        boolean isStorageStateExists = TextUtils.equals(storageState, Environment.MEDIA_MOUNTED);

        if(!isStorageStateExists) {
            Logger.w(TAG, "external storage state not exits.");
            return "";
        } else {
            return Environment.getExternalStorageDirectory().getPath();
        }
    }
}
