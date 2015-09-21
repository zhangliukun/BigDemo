package com.zlk.bigdemo.application.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.util.UUID;

public class FileUtils {

    private static final String DIR_IMAGE = "local_image";
    private static final String DIR_RECORD = "local_record";
    private static final String FRESCO_CACHE = "fresco_img";
    private static final String DIR_DATA_ROOT = "/data/data/";
    private static final String DIR_INTERNAL_CACHE = "cache";


    public static String getInternalCache(Context context){
        String path = DIR_DATA_ROOT+context.getPackageName();
        if (!TextUtils.isEmpty(path)) {
            String dir = path + File.separator + DIR_INTERNAL_CACHE;
            File file = new File(dir);
            if (!file.exists()) {
                file.mkdir();
            }
            return file.getAbsolutePath();
        }
        return "";
    }

    public static String getFrescoCache(Context context) {
        String path = getCachePath(context);
        if (!TextUtils.isEmpty(path)) {
            String dir = path + File.separator + FRESCO_CACHE;
            File file = new File(dir);
            if (!file.exists()) {
                file.mkdir();
            }
            return file.getAbsolutePath();
        }
        return "";
    }

    public static String getImageCachePath(Context context) {
        String path = getCachePath(context);
        if (!TextUtils.isEmpty(path)) {
            String root = path + File.separator + DIR_IMAGE;
            File file = new File(root);
            if (!file.exists()) {
                file.mkdir();
            }
            return file.getAbsolutePath();
        }
        return "";
    }

    public static File newImageCacheFile(Context context) {
        String root = getImageCachePath(context);
        if (!TextUtils.isEmpty(root)) {
            return new File(root, UUID.randomUUID().toString() + ".jpg");
        }
        return null;
    }

    public static String getCachePath(Context context) {
        String state = Environment.getExternalStorageState();
        String path = "";
        if (state != null && state.equals(Environment.MEDIA_MOUNTED)) {

            if (Build.VERSION.SDK_INT >= 8) {
                File file = context.getExternalCacheDir();
                if (file != null) {
                    path = file.getAbsolutePath();
                }
                if (TextUtils.isEmpty(path)) {
                    path = Environment.getExternalStorageDirectory()
                            .getAbsolutePath();
                }
            } else {
                path = Environment.getExternalStorageDirectory()
                        .getAbsolutePath();
            }
        } else if (context.getCacheDir() != null) {
            path = context.getCacheDir().getAbsolutePath();
        }
        return path;
    }

    public static File getRecordPath(Context context) {
        String path = getCachePath(context);
        if (!TextUtils.isEmpty(path)) {
            String root = path + File.separator + DIR_RECORD;
            File file = new File(root);
            if (!file.exists()) {
                file.mkdir();
            }
            return file;
        }
        return null;
    }

    public static void writeObject(Context context, Object obj, String fileName) {
        String path = getCachePath(context) + File.separator + fileName;
        FileOutputStream fout = null;
        ObjectOutputStream oos = null;
        try {
            fout = new FileOutputStream(path);
            oos = new ObjectOutputStream(fout);
            oos.writeObject(obj);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fout != null) {
                try {
                    fout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static Object readObject(Context context, String fileName) {
        String path = getCachePath(context) + File.separator + fileName;
        FileInputStream fileInputStream = null;
        ObjectInputStream input = null;
        try {
            fileInputStream = new FileInputStream(new File(path));
            input = new ObjectInputStream(fileInputStream);
            return input.readObject();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    public static void deleteFile(String path) {
        try {
            File f = new File(path);
            if (f.isDirectory()) {
                File[] file = f.listFiles();
                if (file != null) {
                    for (File file2 : file) {
                        deleteFile(file2.toString());
                        file2.delete();
                    }
                }
            } else {
                f.delete();
            }
            f.delete();
        } catch (Exception e) {

        }
    }

    public static byte[] getFileBytes(String pathName) {
        File file = new File(pathName);
        FileInputStream fis = null;
        byte[] retBytes = null;
        if (file.exists()) {
            try {
                fis = new FileInputStream(file);
                int len = fis.available();
                retBytes = new byte[len];
                fis.read(retBytes);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return retBytes;
    }

    public static String getFileMimeType(String path) {
        File f = new File(path);
        try {
            return f.toURL().openConnection().getContentType();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 检测内存卡是否可用
     */
    public static boolean isSDcardUsable() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isFileExist(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取内存卡路径
     */
    public static File getSDcardDir() {
        if (isSDcardUsable()) {
            return Environment.getExternalStorageDirectory();
        } else {
            return null;
        }
    }

    public static String getEntryPath(Context context, String dirName) {
        String path = getCachePath(context);
        if (!TextUtils.isEmpty(path)) {
            String root = path + File.separator + dirName;
            File file = new File(root);
            if (!file.exists()) {
                file.mkdir();
            }
            return file.getAbsolutePath();
        }
        return null;
    }

    public static void writeFile(byte[] bfile, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {//判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public static String getFileOrFilesSize(String filePath) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertFileSize(blockSize);
    }


    /**
     * 获取指定文件大小
     *
     */
    public static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
        }
        return size;
    }

    /**
     * 获取指定文件夹
     *
     * @param f
     * @return
     * @throws Exception
     */
    public static long getFileSizes(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSizes(flist[i]);
            } else {
                size = size + getFileSize(flist[i]);
            }
        }
        return size;
    }

    /**
     * 转换文件大小,指定转换的类型
     *
     * @return
     */
    public static String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
        DecimalFormat df = new DecimalFormat("#.00");
        double formatSize = 0d;
        if (size >= gb) {
            formatSize = Double.valueOf(df.format(size * 1.0 / gb));
            return formatSize + "GB";
        } else if (size >= mb) {
            formatSize = Double.valueOf(df.format(size * 1.0 / mb));
            return formatSize + "MB";
        } else {
            formatSize = Double.valueOf(df.format(size * 1.0 / kb));
            if(size == 0)
                return "0KB";
            return formatSize + "KB";
        }
    }
}
