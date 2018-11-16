package com.process.ffmpeg_shell.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.process.ffmpeg_shell.log.Logger;

/**
 * 图片加载工具类
 * Created by kerwin on 2018/11/16
 */
public class ImageViewLoadUtils {
    private static final String TAG = "ImageViewLoadUtils";

    /**
     * 将图片到图片加载到组件中
     * @param view 图片组件对象
     * @param path 图片文件目录
     * */
    public static void loadImageView(final ImageView view, final String path) {
        new AsyncTask<Void, Void, Bitmap>() {

            @Override
            protected Bitmap doInBackground(Void... voids) {
                BitmapFactory.Options option = new BitmapFactory.Options();
                option.inPreferredConfig = Bitmap.Config.ARGB_8888;
                return BitmapFactory.decodeFile(path);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if(view == null) {
                    Logger.w(TAG, "onCommandProviderFinish() mLoadImageView == null.");
                } else {
                    view.setImageBitmap(bitmap);
                }
            }
        }.execute();
    }
}
