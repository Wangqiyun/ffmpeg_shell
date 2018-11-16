package com.process.ffmpeg_shell.terminal;

import android.content.Context;

/**
 * Created by kerwin on 2018/11/14
 */
public class FFmpegFunTerminalTask extends AbsFunTerminalTask {
    private static final String TAG = "FFmpegFunTerminalTask";

    private static final String ASSET_FILE_NAME = "ffmpeg";

    public FFmpegFunTerminalTask(Context context) {
        super(context);
    }

    @Override
    protected String obtainAssetsFileName() {
        return ASSET_FILE_NAME;
    }

    @Override
    protected boolean accreditFunTerminalFile(String path) {
        return super.accreditFunTerminalFile(path);
    }
}
