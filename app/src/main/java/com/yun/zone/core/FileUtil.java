package com.yun.zone.core;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
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
        String strContent = new StringBuffer(content).append("\n").toString();
        try {
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e) {

        }
    }

    protected static void update(File file, String content, int line) {
        int num = 0;
        StringBuffer stringBuffer = new StringBuffer();
        InputStreamReader read = null;
        try {
            read = new InputStreamReader(
                    new FileInputStream(file));
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                num = num + 1;
                if (num != line) {
                    stringBuffer.append(lineTxt).append("\n");
                } else {
                    stringBuffer.append(content).append("\n");
                }
            }
            read.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.write(stringBuffer.toString().getBytes());
            raf.close();
        } catch (Exception e) {

        }

    }


    protected static void deleteLine(File file, int line) {
        int num = 0;
        StringBuffer stringBuffer = new StringBuffer();
        InputStreamReader read = null;
        try {
            read = new InputStreamReader(
                    new FileInputStream(file));
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                num = num + 1;
                if (num != line) {
                    stringBuffer.append(lineTxt).append("\n");
                }
            }
            read.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String path = file.getPath();
        file.delete();
        file = new File(path);
        try {
            file.createNewFile();
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.write(stringBuffer.toString().getBytes());
            raf.close();
        } catch (IOException e) {
            e.printStackTrace();
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
