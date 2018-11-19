package com.process.ffmpeg_shell;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.process.ffmpeg_shell.log.Logger;
import com.process.ffmpeg_shell.provider.AbsCommandProvider;
import com.process.ffmpeg_shell.provider.ffmpeg.FFmpegCommandProvider;
import com.process.ffmpeg_shell.utils.ImageViewLoadUtils;

import java.io.File;

public class DemoCommandActivity extends AppCompatActivity implements
        AbsCommandProvider.OnCommandProviderListener,
        View.OnClickListener {
    private static final String TAG = "DemoCommandActivity";

    private FFmpegCommandProvider mFFmpegCommandProvider;

    private long mLoadImageSession = 0;
    private ImageView mLoadImageView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ffmpeg_command);

        mFFmpegCommandProvider = new FFmpegCommandProvider(this);
        mFFmpegCommandProvider.setOnCommandProviderListener(DemoCommandActivity.this);

        mLoadImageView = (ImageView) findViewById(R.id.screen_shots_image);

        findViewById(R.id.get_ffmpeg_version).setOnClickListener(this);
        findViewById(R.id.get_screen_shots).setOnClickListener(this);
        findViewById(R.id.resolution).setOnClickListener(this);
    }

    @Override
    public void onCommandProviderFinish(long session, Bundle extra) {
        if(mLoadImageSession != session) {
            return;
        }

        if(extra == null) {
            Logger.w(TAG, "onCommandProviderFinish() extra not is null.");
            return;
        }

        String filePath = extra.getString(FFmpegCommandProvider.FFmpegCommandKeys.OUTPUT);
        if(TextUtils.isEmpty(filePath)) {
            Logger.w(TAG, "onCommandProviderFinish() path not is empty.");
            return;
        }

        ImageViewLoadUtils.loadImageView(mLoadImageView, filePath);
    }

    @Override
    public void onCommandProviderProgress(long session, String result) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mFFmpegCommandProvider == null) {
            Logger.i(TAG, "onDestroy() mFFmpegCommandProvider == null.");
        } else {
            mFFmpegCommandProvider.release();
        }
    }

    @Override
    public void onClick(View v) {
        if(mFFmpegCommandProvider == null) {
            Logger.w(TAG, "onClick() provider is null.");
            return;
        }

        int viewId = v.getId();
        if(viewId == R.id.get_ffmpeg_version) {
            mFFmpegCommandProvider.getVersion();
        } else if(viewId == R.id.get_screen_shots) {
            Bundle bundle = new Bundle();
            bundle.putString(FFmpegCommandProvider.FFmpegCommandKeys.INPUT, getSDCardFilePath("demo.mp4"));
            bundle.putString(FFmpegCommandProvider.FFmpegCommandKeys.OUTPUT, getSDCardFilePath("demo.jpg"));
            bundle.putString(FFmpegCommandProvider.FFmpegCommandKeys.TIME, "00:00:02");

            mLoadImageSession = mFFmpegCommandProvider.generateScreenShots(bundle);
        } else if(viewId == R.id.resolution) {
            Bundle bundle = new Bundle();
            bundle.putString(FFmpegCommandProvider.FFmpegCommandKeys.INPUT, getSDCardFilePath("demo.mp4"));
            bundle.putString(FFmpegCommandProvider.FFmpegCommandKeys.OUTPUT, getSDCardFilePath("resolution.mp4"));
            bundle.putString(FFmpegCommandProvider.FFmpegCommandKeys.RESOLUTION, "800x480");

            mFFmpegCommandProvider.compressionVideoResolution(bundle);
        }
    }

    public String getSDCardFilePath(String fileName) {
        if(mFFmpegCommandProvider == null) {
            Logger.w(TAG, "getSDCardFilePath() provider not is null.");
            return "";
        }

        return mFFmpegCommandProvider.getProviderCacheDir(this) + File.separator + fileName;
    }
}
