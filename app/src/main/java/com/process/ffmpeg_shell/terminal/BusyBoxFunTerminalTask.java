package com.process.ffmpeg_shell.terminal;

import android.content.Context;

/**
 * Created by kerwin on 2018/11/14
 */
public class BusyBoxFunTerminalTask extends AbsFunTerminalTask {
    private static final String TAG = "BusyBoxFunTerminalTask";

    private static final String ASSET_FILE_NAME = "busybox";

    public BusyBoxFunTerminalTask(Context context) {
        super(context);
    }

    @Override
    protected String obtainAssetsFileName() {
        return ASSET_FILE_NAME;
    }
}
