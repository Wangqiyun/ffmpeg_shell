package com.process.ffmpeg_shell.log;

import android.util.Log;

/**
 * 功能模块日志处理
 * Created by kerwin on 2018/11/12
 */
public class Logger {
    private static ILogger LOGGER = new DefLogger();
    private static boolean IS_ENABLED = true;

    private static final int LOG_LEVEL_V = 1;
    private static final int LOG_LEVEL_D = 2;
    private static final int LOG_LEVEL_I = 3;
    private static final int LOG_LEVEL_W = 4;
    private static final int LOG_LEVEL_E = 5;

    /** 表示符合大于>=指定level时输出log */
    private static int LEVEL = LOG_LEVEL_V;

    public static int v(String tag, String msg) {
        return isPrintLog(LOG_LEVEL_V) ? LOGGER.v(tag, msg) : 0;
    }


    public static int v(String tag, String msg, Throwable tr) {
        return isPrintLog(LOG_LEVEL_V) ? LOGGER.v(tag, msg, tr) : 0;
    }


    public static int d(String tag, String msg) {
        return isPrintLog(LOG_LEVEL_D) ? LOGGER.v(tag, msg) : 0;
    }


    public static int d(String tag, String msg, Throwable tr) {
        return isPrintLog(LOG_LEVEL_D) ? LOGGER.d(tag, msg, tr) : 0;
    }


    public static int i(String tag, String msg) {
        return isPrintLog(LOG_LEVEL_I) ? LOGGER.i(tag, msg) : 0;
    }


    public static int i(String tag, String msg, Throwable tr) {
        return isPrintLog(LOG_LEVEL_I) ? LOGGER.i(tag, msg, tr) : 0;
    }


    public static int w(String tag, String msg) {
        return isPrintLog(LOG_LEVEL_W) ? LOGGER.w(tag, msg) : 0;
    }


    public static int w(String tag, String msg, Throwable tr) {
        return isPrintLog(LOG_LEVEL_W) ? LOGGER.w(tag, msg, tr) : 0;
    }


    public static int w(String tag, Throwable tr) {
        return isPrintLog(LOG_LEVEL_W) ? LOGGER.w(tag, tr) : 0;
    }


    public static int e(String tag, String msg) {
        return isPrintLog(LOG_LEVEL_E) ? LOGGER.e(tag, msg) : 0;
    }


    public static int e(String tag, String msg, Throwable tr) {
        return isPrintLog(LOG_LEVEL_E) ? LOGGER.e(tag, msg, tr) : 0;
    }

    /**
     * 设置接收处理logger实体
     * @param logger logger实体
     * */
    public static void setLogger(ILogger logger) {
        LOGGER = logger;
    }

    /**
     * 设置启动Logger打印
     * @param isEnabled 标记是否开启日志打印
     * */
    public static void setEnabled(boolean isEnabled) {
        IS_ENABLED = isEnabled;
    }

    /**
     * 判断是否打印log
     * @return 如果返回true表示打印,反之表示不打印
     * */
    private static boolean isPrintLog(int level) {
        return LOGGER != null && IS_ENABLED && level >= LEVEL;
    }

    public interface ILogger {
        int v(String tag, String msg);
        int v(String tag, String msg, Throwable tr);
        int d(String tag, String msg);
        int d(String tag, String msg, Throwable tr);
        int i(String tag, String msg);
        int i(String tag, String msg, Throwable tr);
        int w(String tag, String msg);
        int w(String tag, String msg, Throwable tr);
        int w(String tag, Throwable tr);
        int e(String tag, String msg);
        int e(String tag, String msg, Throwable tr);
    }

    private static class DefLogger implements ILogger {

        @Override
        public int v(String tag, String msg) {
            return Log.v(tag, msg);
        }

        @Override
        public int v(String tag, String msg, Throwable tr) {
            return Log.v(tag, msg, tr);
        }

        @Override
        public int d(String tag, String msg) {
            return Log.d(tag, msg);
        }

        @Override
        public int d(String tag, String msg, Throwable tr) {
            return Log.d(tag, msg, tr);
        }

        @Override
        public int i(String tag, String msg) {
            return Log.i(tag, msg);
        }

        @Override
        public int i(String tag, String msg, Throwable tr) {
            return Log.i(tag, msg, tr);
        }

        @Override
        public int w(String tag, String msg) {
            return Log.w(tag, msg);
        }

        @Override
        public int w(String tag, String msg, Throwable tr) {
            return Log.w(tag, msg, tr);
        }

        @Override
        public int w(String tag, Throwable tr) {
            return Log.w(tag, tr);
        }

        @Override
        public int e(String tag, String msg) {
            return Log.e(tag, msg);
        }

        @Override
        public int e(String tag, String msg, Throwable tr) {
            return Log.e(tag, msg, tr);
        }
    }
}
