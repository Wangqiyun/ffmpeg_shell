package com.process.ffmpeg_shell.provider.ffmpeg;

import android.content.Context;
import android.os.Bundle;

import com.process.ffmpeg_shell.common.AssetFileStorageHandler;
import com.process.ffmpeg_shell.log.Logger;
import com.process.ffmpeg_shell.terminal.FFmpegFunTerminalTask;
import com.process.ffmpeg_shell.terminal.IInstallFunTerminalTask;
import com.process.ffmpeg_shell.provider.AbsCommandProvider;
import com.process.ffmpeg_shell.utils.CacheStorageUtils;

import java.io.File;

/**
 * ffmpeg命令支持者实现类
 * Created by kerwin on 2018/11/14
 */
public class FFmpegCommandProvider extends AbsCommandProvider implements AssetFileStorageHandler.OnAssetFileStorageListener {
    private static final String TAG = "FFmpegCommandProvider";

    public FFmpegCommandProvider(Context context) {
        super(context);
    }

    @Override
    public IInstallFunTerminalTask generateInstallFunTerminalTask(Context context) {
        return new FFmpegFunTerminalTask(context);
    }

    @Override
    public void initialized(Context context) {
        String parentPath = getProviderCacheDir(context);
        AssetFileStorageHandler handler = new AssetFileStorageHandler();
        handler.setOnAssetFileStorageListener(this);
        handler.asyncSaveAssetFileToStorage(context, "demo.mp4", parentPath);
    }

    @Override
    public String getProviderCacheDir(Context context) {
        if(context == null) {
            Logger.w(TAG, "getProviderCacheDir() context is null.");
            return "";
        }

        String externalStoragePath = CacheStorageUtils.obtainExternalStorageDir(context);
        return externalStoragePath + File.separator + "demo";
    }

    /**
     * 通过该方法取得ffmpeg的版本号
     * */
    public long getVersion() {
        return queueCommandExecute(null, buildCommandList(new String[]{"-version"}));
    }

    /**
     * 指定文件路径进行视频分辨率压缩
     * @param bundle 进行视频分辨率压缩的bundle值
     * */
    public long compressionVideoResolution(Bundle bundle) {
        FFmpegCommandKeys keys = FFmpegCommandKeys.parse(bundle);
        if(keys == null) {
            Logger.w(TAG, "generateScreenShots() keys not is null.");
            return 0L;
        }

        return queueCommandExecute(bundle, buildCommandList(new String[]{
                FFmpegArgs.COMMAND_I,
                keys.input,
                FFmpegArgs.VIDEO_S,
                keys.resolution,
                keys.output
        }));
    }

    /**
     * 将ts文件转换到mp4
     * @param bundle 进行ts文件转换到mp4的bundle值
     * */
    public long transcodeTsToMp4(Bundle bundle) {
        FFmpegCommandKeys keys = FFmpegCommandKeys.parse(bundle);
        if(keys == null) {
            Logger.w(TAG, "generateScreenShots() keys not is null.");
            return 0L;
        }

        return queueCommandExecute(null, buildCommandList(new String[]{
                FFmpegArgs.COMMAND_I,
                keys.input,
                FFmpegArgs.VIDEO_VCODEC,
                "h264",
                FFmpegArgs.VOICE_ACODEC,
                "aac",
                FFmpegArgs.VIDEO_HIGH_STRICT,
                "-2",
                keys.output
        }));
    }

    /**
     * 生成指定时间视频截图
     * @param bundle 进行视频截图的bundle值
     * */
    public long generateScreenShots(Bundle bundle) {
        FFmpegCommandKeys keys = FFmpegCommandKeys.parse(bundle);
        if(keys == null) {
            Logger.w(TAG, "generateScreenShots() keys not is null.");
            return 0L;
        }

        return queueCommandExecute(bundle, buildCommandList(new String[]{
                FFmpegArgs.COMMAND_SS,
                keys.time,
                FFmpegArgs.COMMAND_I,
                keys.input,
                "-vframes",
                "1",
                FFmpegArgs.COMMAND_Y,
                keys.output
        }));
    }

    @Override
    public void onAssetFileStorageFinish(String path) {
        Logger.i(TAG, "onAssetFileStorageFinish() path > " + path);
    }

    /** ffmpeg模块的bundle key */
    public static class FFmpegCommandKeys {
        /** 输入路径的key值 */
        public static final String INPUT = "input";
        /** 输出路径的key值 */
        public static final String OUTPUT = "output";
        /** 时间的key值 */
        public static final String TIME = "time";
        /** 分辨率字符串(如:1280x720) */
        public static final String RESOLUTION = "resolution";

        public String input = "";
        public String output = "";
        public String time = "";
        public String resolution = "";

        public static FFmpegCommandKeys parse(Bundle bundle) {
            FFmpegCommandKeys keys = new FFmpegCommandKeys();

            if(bundle == null) {
                Logger.w(TAG, "parse() bundle not is null.");
                return null;
            }

            keys.input = bundle.getString(INPUT);
            keys.output = bundle.getString(OUTPUT);
            keys.time = bundle.getString(TIME);
            keys.resolution = bundle.getString(RESOLUTION);
            return keys;
        }
    }
}
