package com.process.ffmpeg_shell.utils;

import android.content.Context;
import android.util.Log;

import java.io.File;

/**
 * 缓存存储支持实现类
 * Created by kerwin on 2018/11/12
 */
public class CacheStorageUtils {
    private static final String TAG = "CacheStorageUtils";

    /**
     * 得到外存储缓存目录
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
}
