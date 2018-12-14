package com.example.wjh.zlb.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import junit.framework.Assert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class BitmapUtil {

    private static final String TAG = "BitmapUtil";

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    // 如果是放大图片，filter决定是否平滑，如果是缩小图片，filter无影响
    private static Bitmap createScaleBitmap(Bitmap src, int dstWidth,
                                            int dstHeight) {
        if (src == null) {
            return null;
        }
        Bitmap dst = Bitmap.createScaledBitmap(src, dstWidth, dstHeight, false);
        if (src != dst) { // 如果没有缩放，那么不回收
            src.recycle(); // 释放Bitmap的native像素数组
        }
        return dst;
    }

    /**
     * 获取Bitmap大小
     *
     * @param bitmap
     * @return
     */
    public static int getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // API 19
            return bitmap.getAllocationByteCount();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {// API
            return bitmap.getByteCount();
        } else {
            return bitmap.getRowBytes() * bitmap.getHeight();
        }
    }

    // 从Resources中加载图片
    public static Bitmap decodeSampledBitmapFromResource(Resources res,
                                                         int resId, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options); // 读取图片长款
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight); // 计算inSampleSize
        options.inJustDecodeBounds = false;
        Bitmap src = BitmapFactory.decodeResource(res, resId, options); // 载入一个稍大的缩略图
        return createScaleBitmap(src, reqWidth, reqHeight); // 进一步得到目标大小的缩略图
    }

    // 从sd卡上加载图片
    public static Bitmap decodeSampledBitmapFromFd(String pathName,
                                                   int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);
        options.inJustDecodeBounds = false;
        Bitmap src = BitmapFactory.decodeFile(pathName, options);
        return createScaleBitmap(src, reqWidth, reqHeight);
    }

    public static Bitmap decodeBitmap(String pathName) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取图片的宽和高
        Bitmap bitmap = BitmapFactory.decodeFile(pathName, options);
        float realWidth = options.outWidth;
        float realHeight = options.outHeight;
        // System.out.println("真实图片高度：" + realHeight + "宽度:" + realWidth);
        // 缩放比
        int scale = (int) ((realHeight > realWidth ? realHeight : realWidth) / 300);
        if (scale <= 0) {
            scale = 1;
        }
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        // 注意这次要把options.inJustDecodeBounds 设为 false,这次图片是要读取出来的。
        bitmap = BitmapFactory.decodeFile(pathName, options);
        // int w = bitmap.getWidth();
        // int h = bitmap.getHeight();
        // System.out.println("缩略图高度：" + h + "宽度:" + w);
        return bitmap;
    }

    public static Bitmap getThumbnail(String pathName, int reqWidth,
                                      int reqHeight) {
        Bitmap bm;
        bm = ThumbnailUtils.extractThumbnail(
                BitmapFactory.decodeFile(pathName), reqWidth, reqHeight);
        return bm;
    }

    public static Bitmap getThumbnail(Bitmap bitmap, int reqWidth, int reqHeight) {
        Bitmap bm;
        bm = ThumbnailUtils.extractThumbnail(bitmap, reqWidth, reqHeight);
        return bm;
    }

    /**
     * 保存方法
     */
    public static void saveBitmap(String path, String name, Bitmap bm,
                                  int quality) {
        File folder = new File(path);
        if (!folder.exists()) {// 如果文件夹不存在则创建
            folder.mkdir();
        }
        File f = new File(path + name);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(CompressFormat.JPEG, quality, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 保存方法
     */
    public static void saveBitmap(String path, String name, Bitmap bitmap,
                                  int reqWidth, int reqHeight, int quality) {
        File folder = new File(path);
        if (!folder.exists()) {// 如果文件夹不存在则创建
            folder.mkdir();
        }
        File f = new File(path + name);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            Bitmap bitmap2 = ThumbnailUtils.extractThumbnail(bitmap, reqWidth,
                    reqHeight);
            bitmap2.compress(CompressFormat.JPEG, quality, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * Get bitmap from specified image path
     *
     * @param imgPath
     * @return
     */
    public static Bitmap getBitmap(String imgPath) {
        // Get bitmap through image path
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = false;
        newOpts.inPurgeable = true;
        newOpts.inInputShareable = true;
        // Do not compress
        newOpts.inSampleSize = 1;
        newOpts.inPreferredConfig = Config.RGB_565;
        return BitmapFactory.decodeFile(imgPath, newOpts);
    }

    /**
     * 获取图片base64格式
     *
     * @param imgPath SD卡路径
     * @return
     */
    public static String getBitmapBase64(String imgPath) {
        Bitmap bitmap = getBitmap(imgPath);
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            bitmap.compress(CompressFormat.JPEG, 100, bos);
            byte[] data1 = bos.toByteArray();
            String strPhoto1 = Base64.encodeToString(data1, Base64.DEFAULT);
            return strPhoto1;
        } catch (Exception e2) {

        }
        return null;
    }


    /**
     * bitbap转base64
     */

    public static String getBitmapBase64(Bitmap bitmap) throws IOException {
        if (bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(CompressFormat.JPEG, 100, baos);
            baos.flush();
            baos.close();
            byte[] bitmapBytes = baos.toByteArray();
            String base = Base64.encodeToString(bitmapBytes, Base64.NO_WRAP);
            return base;
        }
        return null;
    }

    /**
     * base64转为bitmap
     *
     * @param base64Data
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }


    /**
     * Store bitmap into specified image path
     *
     * @param bitmap
     * @param outPath
     * @throws FileNotFoundException
     */
    public static void storeImage(Bitmap bitmap, String outPath)
            throws FileNotFoundException {
        FileOutputStream os = new FileOutputStream(outPath);
        bitmap.compress(CompressFormat.JPEG, 100, os);
    }

    /**
     * Compress image by pixel, this will modify image width/height. Used to get
     * thumbnail
     *
     * @param imgPath image path
     * @param pixelW  target pixel of width
     * @param pixelH  target pixel of height
     * @return
     */
    public static Bitmap ratio(String imgPath, float pixelW, float pixelH) {
        int degree = BitmapUtil.readPictureDegree(imgPath);
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true，即只读边不读内容
        newOpts.inJustDecodeBounds = true;
        newOpts.inPreferredConfig = Config.RGB_565;
        // Get bitmap info, but notice that bitmap is null now
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath, newOpts);

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 想要缩放的目标尺寸
        float hh = pixelH;// 设置高度为240f时，可以明显看到图片缩小了
        float ww = pixelW;// 设置宽度为120f，可以明显看到图片缩小了
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w >= h && ww < w) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w <= h && hh < h) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 开始压缩图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(imgPath, newOpts);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 100, os);
        //LogUtil.e(TAG, "图片大小:" +os.toByteArray().length);
        // 压缩好比例大小后再进行质量压缩
        // return compress(bitmap, maxSize); // 这里再进行质量压缩的意义不大，反而耗资源，删除
        return rotateToDegrees(bitmap, degree);
    }

    /**
     * Compress image by size, this will modify image width/height. Used to get
     * thumbnail
     *
     * @param image
     * @param pixelW target pixel of width
     * @param pixelH target pixel of height
     * @return
     */
    public static Bitmap ratio(Bitmap image, float pixelW, float pixelH) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        if (image == null) {
            throw new RuntimeException("传入的图片不能为空");
        }
        image.compress(CompressFormat.JPEG, 100, os);

        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        newOpts.inPreferredConfig = Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = pixelH;// 设置高度为240f时，可以明显看到图片缩小了
        float ww = pixelW;// 设置宽度为120f，可以明显看到图片缩小了
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = Math.round(newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = Math.round(newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        is = new ByteArrayInputStream(os.toByteArray());
        bitmap = BitmapFactory.decodeStream(is, null, newOpts);
        // 压缩好比例大小后再进行质量压缩
        //return compress(bitmap, maxSize); // 这里再进行质量压缩的意义不大，反而耗资源，删除
        return bitmap;
    }

    /**
     * @param bitmap
     * @param maxSize   最大大小
     * @param frequency 压缩次数
     * @return
     */
    public static Bitmap compress(Bitmap bitmap, float maxSize, int frequency) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 100, os);
        int s = os.size();
        int b = os.toByteArray().length / 1024;
        int length = 0;
        while (b > maxSize && length <= frequency) {
            bitmap.compress(CompressFormat.JPEG, 10, os);
            b = os.toByteArray().length / 1024;
            length++;
        }

        return BitmapFactory.decodeByteArray(os.toByteArray(), 0, os.toByteArray().length);


    }


    /**
     * @param bitMap
     * @param maxSize 压缩后图片的最大存储大小
     * @return
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    @SuppressLint("NewApi")
    static public Bitmap imageZoom(Bitmap bitMap, double maxSize) {
        // 将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int size = getBitmapSize(bitMap);
        bitMap.compress(CompressFormat.JPEG, 100, baos);

        if (size / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();// 重置baos即清空baos
            int p = 50;
            bitMap.compress(CompressFormat.JPEG, p, baos);// 这里压缩50%，把压缩后的数据存放到baos中
        } else {
//			bitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        }


        int b = 0;
        // 将字节换成KB
        b = baos.toByteArray().length;
        double mid = b / 1024;

        if (mid < maxSize) {
            return BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.toByteArray().length);
        }

        while (mid > maxSize + 10) {
            double i = mid / maxSize;
            // 开始压缩 此处用到平方根 将宽带和高度压缩掉对应的平方根倍
            // （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）
            bitMap = zoomImg(bitMap, bitMap.getWidth() / Math.sqrt(i),
                    bitMap.getHeight() / Math.sqrt(i));
            bitMap.compress(CompressFormat.JPEG, 100, baos);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // API 19
                b = bitMap.getAllocationByteCount();
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {// API 12
                b = bitMap.getByteCount();
            }
            mid = b / 1024;
        }
        return bitMap;
    }

    /**
     * @param imgPath 图片路径
     * @param maxSize 压缩后图片的最大存储大小
     * @return
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    @SuppressLint("NewApi")
    static public Bitmap imageZoom(String imgPath, double maxSize) {
        Bitmap bitMap = null;
        //将图片转为 RGB_565
        BitmapFactory.Options options2 = new BitmapFactory.Options();
        options2.inPreferredConfig = Config.RGB_565;
        bitMap = BitmapFactory.decodeFile(imgPath, options2);


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int size = getBitmapSize(bitMap);
        bitMap.compress(CompressFormat.JPEG, 100, baos);

        // 将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）
        if (size / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();// 重置baos即清空baos
            double i = size / 1024 / maxSize;

            int p = 50;
            bitMap.compress(CompressFormat.JPEG, p, baos);// 这里压缩30%，把压缩后的数据存放到baos中
        } else {
//			bitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        }
        int b = 0;
        // 将字节换成KB
        b = baos.toByteArray().length;
        double mid = b / 1024;

        while (mid > maxSize + 10) {
            double i = mid / maxSize;
            // 开始压缩 此处用到平方根 将宽带和高度压缩掉对应的平方根倍
            // （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）
            bitMap = zoomImg(bitMap, bitMap.getWidth() / Math.sqrt(i),
                    bitMap.getHeight() / Math.sqrt(i));
            bitMap.compress(CompressFormat.JPEG, 100, baos);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // API 19
                b = bitMap.getAllocationByteCount();
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {// API 12
                b = bitMap.getByteCount();
            }
            mid = b / 1024;

        }
        return bitMap;
    }


    /**
     * 等比例缩放图片
     */
    public static Bitmap zoomImg(Bitmap bm, double newWidth, double newHeight) {
        // 获得图片的宽高
        float width = bm.getWidth();
        float height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, (int) width, (int) height,
                matrix, true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        newbm.compress(CompressFormat.JPEG, 100, baos);
        bm.recycle();
        return newbm;
    }

    /**
     * Compress by quality, and generate image to the path specified
     *
     * @param image
     * @param outPath
     * @param maxSize target will be compressed to be smaller than this size.(kb)
     * @throws IOException
     */
    public static void compressAndGenImage(Bitmap image, String outPath, String name,
                                           int maxSize) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        // scale
        int options = 100;
        // Store the bitmap into output stream(no compress)
        image.compress(CompressFormat.JPEG, options, os);
        // Compress by loop
        while (os.toByteArray().length / 1024 > maxSize) {
            // Clean up os
            os.reset();
            // interval 10
            options -= 10;
            image.compress(CompressFormat.JPEG, options, os);
        }
        /*File dir = new File(outPath);
        if (!dir.exists()) {
            try {
                //在指定的文件夹中创建文件
                dir.createNewFile();
            } catch (Exception e) {
                e.getMessage();
            }
        }*/
        Bitmap bitmap = BitmapFactory.decodeByteArray(os.toByteArray(), 0, os.toByteArray().length);
        saveBitmap(outPath, name, bitmap, 100);
        bitmap.recycle();
    }

    /**
     * Compress by quality, and generate image to the path specified
     *
     * @param image
     * @param outPath
     * @param maxSize target will be compressed to be smaller than this size.(kb)
     * @throws IOException
     */
    public static void compressAndGenImage(Bitmap image, String outPath,
                                           int maxSize) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        // scale
        int options = 100;
        // Store the bitmap into output stream(no compress)
        image.compress(CompressFormat.JPEG, options, os);
        // Compress by loop
        while (os.toByteArray().length / 1024 > maxSize) {
            // Clean up os
            os.reset();
            // interval 10
            options -= 10;
            image.compress(CompressFormat.JPEG, options, os);
        }
        /*File dir = new File(outPath);
        if (!dir.exists()) {
            try {
                //在指定的文件夹中创建文件
                dir.createNewFile();
            } catch (Exception e) {
                e.getMessage();
            }
        }*/

        // 打开路径文件
        FileOutputStream fos = new FileOutputStream(outPath, true);
        fos.write(os.toByteArray());
        fos.flush();
        fos.close();
    }

    /**
     * Compress by quality, and generate image to the path specified
     *
     * @param imgPath
     * @param outPath
     * @param maxSize     target will be compressed to be smaller than this size.(kb)
     * @param needsDelete Whether delete original file after compress
     * @throws IOException
     */
    public static void compressAndGenImage(String imgPath, String outPath,
                                           int maxSize, boolean needsDelete) throws IOException {
        compressAndGenImage(getBitmap(imgPath), outPath, maxSize);

        // Delete original file
        if (needsDelete) {
            File file = new File(imgPath);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * Ratio and generate thumb to the path specified
     *
     * @param image
     * @param outPath
     * @param pixelW  target pixel of width
     * @param pixelH  target pixel of height
     * @throws FileNotFoundException
     */
    public static void ratioAndGenThumb(Bitmap image, String outPath,
                                        float pixelW, float pixelH) throws FileNotFoundException {
        Bitmap bitmap = ratio(image, pixelW, pixelH);
        storeImage(bitmap, outPath);
    }

    /**
     * Ratio and generate thumb to the path specified
     *
     * @param imgPath
     * @param outPath
     * @param pixelW      target pixel of width
     * @param pixelH      target pixel of height
     * @param needsDelete Whether delete original file after compress
     * @throws FileNotFoundException
     */
    public static void ratioAndGenThumb(String imgPath, String outPath,
                                        float pixelW, float pixelH, boolean needsDelete)
            throws FileNotFoundException {
        Bitmap bitmap = ratio(imgPath, pixelW, pixelH);
        storeImage(bitmap, outPath);

        // Delete original file
        if (needsDelete) {
            File file = new File(imgPath);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * 图片旋转
     *
     * @param tmpBitmap
     * @param degrees
     * @return
     */
    public static Bitmap rotateToDegrees(Bitmap tmpBitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.setRotate(degrees);
        return tmpBitmap = Bitmap.createBitmap(tmpBitmap, 0, 0,
                tmpBitmap.getWidth(), tmpBitmap.getHeight(), matrix, true);
    }

    /**
     * 读取照片exif信息中的旋转角度
     *
     * @param path 照片路径
     * @return角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    //------------------------------------------下面开始是微信开发SDK提供的方法↓

    public static byte[] bmpToByteArray(Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Bitmap bitmap2 = getThumbnail(bmp, 77, 77);
        Bitmap bitmap3 = Bitmap.createBitmap(bitmap2.getWidth(), bitmap2.getHeight(), bitmap2.getConfig());
        Canvas canvas = new Canvas(bitmap3);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap2, 0, 0, null);
        bitmap3.compress(CompressFormat.JPEG, 100, output);
        bitmap2.recycle();
        bitmap3.recycle();

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static byte[] getHtmlByteArray(final String url) {
        URL htmlUrl = null;
        InputStream inStream = null;
        try {
            htmlUrl = new URL(url);
            URLConnection connection = htmlUrl.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inStream = httpConnection.getInputStream();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] data = inputStreamToByte(inStream);

        return data;
    }

    public static byte[] inputStreamToByte(InputStream is) {
        try {
            ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
            int ch;
            while ((ch = is.read()) != -1) {
                bytestream.write(ch);
            }
            byte imgdata[] = bytestream.toByteArray();
            bytestream.close();
            return imgdata;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static byte[] readFromFile(String fileName, int offset, int len) {
        if (fileName == null) {
            return null;
        }

        File file = new File(fileName);
        if (!file.exists()) {
            Log.i(TAG, "readFromFile: file not found");
            return null;
        }

        if (len == -1) {
            len = (int) file.length();
        }

        Log.d(TAG, "readFromFile : offset = " + offset + " len = " + len + " offset + len = " + (offset + len));

        if (offset < 0) {
            Log.e(TAG, "readFromFile invalid offset:" + offset);
            return null;
        }
        if (len <= 0) {
            Log.e(TAG, "readFromFile invalid len:" + len);
            return null;
        }
        if (offset + len > (int) file.length()) {
            Log.e(TAG, "readFromFile invalid file len:" + file.length());
            return null;
        }

        byte[] b = null;
        try {
            RandomAccessFile in = new RandomAccessFile(fileName, "r");
            b = new byte[len]; // 创建合适文件大小的数组
            in.seek(offset);
            in.readFully(b);
            in.close();

        } catch (Exception e) {
            Log.e(TAG, "readFromFile : errMsg = " + e.getMessage());
            e.printStackTrace();
        }
        return b;
    }

    private static final int MAX_DECODE_PICTURE_SIZE = 1920 * 1440;

    public static Bitmap extractThumbNail(final String path, final int height,
                                          final int width, final boolean crop) {
        Assert.assertTrue(path != null && !path.equals("") && height > 0 && width > 0);

        BitmapFactory.Options options = new BitmapFactory.Options();

        try {
            options.inJustDecodeBounds = true;
            Bitmap tmp = BitmapFactory.decodeFile(path, options);
            if (tmp != null) {
                tmp.recycle();
                tmp = null;
            }

            Log.d(TAG, "extractThumbNail: round=" + width + "x" + height + ", crop=" + crop);
            final double beY = options.outHeight * 1.0 / height;
            final double beX = options.outWidth * 1.0 / width;
            Log.d(TAG, "extractThumbNail: extract beX = " + beX + ", beY = " + beY);
            options.inSampleSize = (int) (crop ? (beY > beX ? beX : beY) : (beY < beX ? beX : beY));
            if (options.inSampleSize <= 1) {
                options.inSampleSize = 1;
            }

            // NOTE: out of memory error
            while (options.outHeight * options.outWidth / options.inSampleSize > MAX_DECODE_PICTURE_SIZE) {
                options.inSampleSize++;
            }

            int newHeight = height;
            int newWidth = width;
            if (crop) {
                if (beY > beX) {
                    newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
                } else {
                    newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
                }
            } else {
                if (beY < beX) {
                    newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
                } else {
                    newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
                }
            }

            options.inJustDecodeBounds = false;

            Log.i(TAG, "bitmap required size=" + newWidth + "x" + newHeight + ", orig=" + options.outWidth + "x" + options.outHeight + ", sample=" + options.inSampleSize);
            Bitmap bm = BitmapFactory.decodeFile(path, options);
            if (bm == null) {
                Log.e(TAG, "bitmap decode failed");
                return null;
            }

            Log.i(TAG, "bitmap decoded size=" + bm.getWidth() + "x" + bm.getHeight());
            final Bitmap scale = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
            if (scale != null) {
                bm.recycle();
                bm = scale;
            }

            if (crop) {
                final Bitmap cropped = Bitmap.createBitmap(bm, (bm.getWidth() - width) >> 1, (bm.getHeight() - height) >> 1, width, height);
                if (cropped == null) {
                    return bm;
                }

                bm.recycle();
                bm = cropped;
                Log.i(TAG, "bitmap croped size=" + bm.getWidth() + "x" + bm.getHeight());
            }
            return bm;

        } catch (final OutOfMemoryError e) {
            Log.e(TAG, "decode bitmap failed: " + e.getMessage());
            options = null;
        }

        return null;
    }
    //------------------------------------------------------微信SDK提供↑


    /**
     * 将View转换为Bitmap
     *
     * @param view
     * @return
     */
    public static Bitmap getBitmapFromView(View view) {
        view.destroyDrawingCache();
        view.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache(true);
        return bitmap;
    }

    public static Bitmap getDrawableToBitamp(Drawable drawable) {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        System.out.println("Drawable转Bitmap");
        Config config =
                drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
                        : Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        //注意，下面三行代码要用到，否在在View或者surfaceview里的canvas.drawBitmap会看不到图
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 返回压缩后的bitmap
     *
     * @param uri
     * @param outPath 输出路径
     * @param name    文件名称（带后缀名）
     * @return
     * @throws IOException
     */
    public static Bitmap getUriBitmap(Activity activity,
                             Uri uri,
                             String outPath,
                             String name,
                             float bitmapWidth,
                             float bitmapHeight) throws IOException {
        if (uri == null) {
            return null;
        }

        InputStream input = activity.getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Config.RGB_565;//optional
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        if (input != null) {
            input.close();
        }

        // 部分情况下bitmap有可能为空
        if (bitmap == null) {
            String path = getRealFilePath(activity, uri);
            bitmap = BitmapUtil.getBitmap(path);
        }

        //Bitmap bitmap = MediaStore.Images.Media.getBitmap(mActivity.getContentResolver() , uri);
        bitmap = ratio(bitmap, bitmapWidth, bitmapHeight);
        BitmapUtil.compressAndGenImage(bitmap, outPath, name, 80);


        return bitmap;
    }


    /**
     * 获取uri路径
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

}
