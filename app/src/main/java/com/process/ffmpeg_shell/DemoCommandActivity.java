package com.process.ffmpeg_shell;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
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
    private String mFilePath = "";

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

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onCommandProviderFinish(long session) {
        if(mLoadImageSession == session) {
            ImageViewLoadUtils.loadImageView(mLoadImageView, mFilePath);
        }
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
            String input = getSDCardFilePath("demo.mp4");
            String output = getSDCardFilePath("demo.jpg");

            mLoadImageSession = mFFmpegCommandProvider.generateScreenShots(input, output, "00:00:02");
        } else if(viewId == R.id.resolution) {
            String input = getSDCardFilePath("demo.mp4");
            String output = getSDCardFilePath("resolution.mp4");

            mFFmpegCommandProvider.compressionVideoResolution(input, output, "800x480");
        }
    }

    public String getSDCardFilePath(String fileName) {
        File sdcardFile = Environment.getExternalStorageDirectory();
        return sdcardFile + File.separator + fileName;
    }
}
