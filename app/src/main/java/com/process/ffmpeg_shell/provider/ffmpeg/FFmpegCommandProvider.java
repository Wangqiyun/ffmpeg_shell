package com.process.ffmpeg_shell.provider.ffmpeg;

import android.content.Context;

import com.process.ffmpeg_shell.terminal.FFmpegFunTerminalTask;
import com.process.ffmpeg_shell.terminal.IInstallFunTerminalTask;
import com.process.ffmpeg_shell.provider.AbsCommandProvider;

/**
 * ffmpeg命令支持者实现类
 * Created by kerwin on 2018/11/14
 */
public class FFmpegCommandProvider extends AbsCommandProvider {
    private static final String TAG = "FFmpegCommandProvider";

    public FFmpegCommandProvider(Context context) {
        super(context);
    }

    @Override
    public IInstallFunTerminalTask generateInstallFunTerminalTask(Context context) {
        return new FFmpegFunTerminalTask(context);
    }

    /**
     * 通过该方法取得ffmpeg的版本号
     * */
    public long getVersion() {
        return queueCommandExecute(buildCommandList(new String[]{"-version"}));
    }

    /**
     * 指定文件路径进行视频分辨率压缩
     * @param input 压缩的目标文件路径
     * @param output 压缩的输出文件路径
     * @param resolution 指定分辨率字符串(如:1280x720)
     * */
    public long compressionVideoResolution(String input, String output, String resolution) {
        return queueCommandExecute(buildCommandList(new String[]{
                FFmpegArgs.COMMAND_I,
                input,
                FFmpegArgs.VIDEO_S,
                resolution,
                output
        }));
    }

    /**
     * 将ts文件转换到mp4
     * @param input 压缩的目标文件路径
     * @param output 压缩的输出文件路径
     * */
    public long transcodeTsToMp4(String input, String output) {
        return queueCommandExecute(buildCommandList(new String[]{
                FFmpegArgs.COMMAND_I,
                input,
                FFmpegArgs.VIDEO_VCODEC,
                "h264",
                FFmpegArgs.VOICE_ACODEC,
                "aac",
                FFmpegArgs.VIDEO_HIGH_STRICT,
                "-2",
                output
        }));
    }

    /**
     * 生成指定时间视频截图
     * @param input 视频目标文件路径
     * @param output 存储截图文件路径
     * @param time 视频截图的位置(如: 00:02:00)
     * */
    public long generateScreenShots(String input, String output, String time) {
        return queueCommandExecute(buildCommandList(new String[]{
                FFmpegArgs.COMMAND_SS,
                time,
                FFmpegArgs.COMMAND_I,
                input,
                "-vframes",
                "1",
                FFmpegArgs.COMMAND_Y,
                output
        }));
    }
}
