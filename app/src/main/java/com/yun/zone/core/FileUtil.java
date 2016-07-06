package com.yun.zone.core;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;

/**
 * Created by dell on 2016/7/6.
 */
public class FileUtil {
    protected static File createDir(Context context, String userName) {
        File cacheDir = context.getCacheDir();
        File dir = new File(new StringBuffer(cacheDir.getPath()).append(File.separator).append(userName).toString());
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    protected static File createFile(Context context, String userName, String fileName) {
        File cacheDir = context.getCacheDir();
        File file = new File(new StringBuffer(cacheDir.getPath()).append(File.separator).append(userName).append(File.separator).append(fileName).toString());
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    protected static void write(String content, File file) {
        String strContent = new StringBuffer(content).append("\n\r").toString();
        try {
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e) {

        }
    }

    protected static String read(File file) {
        String res = "";
        try {
            FileInputStream fin = new FileInputStream(file);
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read(buffer);
            res = new String(buffer, "UTF-8");
            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    protected static File[] getFiles(Context context, String userName) {
        return createDir(context, userName).listFiles();
    }

}
