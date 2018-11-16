package com.process.ffmpeg_shell.terminal.common;

import com.process.ffmpeg_shell.log.Logger;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by kerwin on 2018/11/14
 */
public class FunTerminalFileDispose {
    private static final String TAG = "FunTerminalFileDispose";

    /**
     * 将输出流内的文件保存到指定目录下
     * @param stream 文件输出流
     * @param path 保存到指定目录下
     * @return 如果返回true表示保存成功,反之表示保存失败
     * */
    public boolean saveInputStreamToCache(InputStream stream, String path) throws IOException {
        File file = createFile(path);

        if(file == null) {
            Logger.w(TAG, "saveInputStreamToCache() file not is null.");
            return false;
        }

        InputStream input = null;
        OutputStream output = null;

        try {
            input = new BufferedInputStream(stream);
            output = new FileOutputStream(file);

            int readLength = 0;
            byte[] buffer = new byte[1024];
            while((readLength = input.read(buffer, 0, buffer.length)) >= 0) {
                output.write(buffer, 0, readLength);
            }
            output.flush();

            return true;
        } catch (Throwable ex) {
            Logger.e(TAG, "ERROR", ex);
            return false;
        } finally {
            CloseStreamClazz.close(input);
            CloseStreamClazz.close(output);
        }
    }

    /**
     * 创建一个新文件
     * @param path 指定裁剪文件路径
     * */
    private File createFile(String path) throws IOException {
        File file = new File(path);
        if(file.exists()) {
            Logger.i(TAG, "createFile() file exists.");
            return file;
        }

        boolean isNewFile = file.createNewFile();
        if(!isNewFile) {
            Logger.i(TAG, "createFile() create file fail");
            return null;
        }

        return file;
    }

    private static class CloseStreamClazz {
        public static void close(InputStream input) {
            if(input == null) {
                Logger.w(TAG, "close() input not is null.", new NullPointerException());
                return;
            }

            try {
                input.close();
            } catch (IOException e) {
                Logger.e(TAG, "ERROR", e);
            }
        }

        public static void close(OutputStream output) {
            if(output == null) {
                Logger.w(TAG, "close output not is null.", new NullPointerException());
                return;
            }

            try {
                output.close();
            } catch (IOException e) {
                Logger.e(TAG, "ERROR", e);
            }
        }
    }
}
