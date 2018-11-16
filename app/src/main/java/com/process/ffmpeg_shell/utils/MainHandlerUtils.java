package com.process.ffmpeg_shell.utils;

import android.os.Handler;
import android.os.Looper;

import com.process.ffmpeg_shell.log.Logger;

/**
 * 主线程切换处理工具
 * Created by kerwin on 2018/11/16
 */
public class MainHandlerUtils {
    private static final String TAG = "MainHandlerUtils";

    public static void post(final Observer observer) {
        if(Thread.currentThread() == Looper.getMainLooper().getThread()) {
            notifyObserver(observer);
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    notifyObserver(observer);
                }
            });
        }
    }

    private static void notifyObserver(Observer observer) {
        if(observer == null) {
            Logger.w(TAG, "notifyObserver() observer == null.");
        } else {
            observer.onObserver();
        }
    }

    public interface Observer {
        void onObserver();
    }
}
