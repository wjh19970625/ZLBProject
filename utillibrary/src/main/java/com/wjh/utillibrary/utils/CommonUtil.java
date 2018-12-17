package com.wjh.utillibrary.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * @Description：常用方法
 * @author:
 * @time：2016/5/19 14:46
 */
public class CommonUtil {

    //获取app信息
    public static String getAppInfo(Context context) {
        try {
            String pkName = context.getPackageName();
            String versionName = context.getPackageManager().getPackageInfo(
                    pkName, 0).versionName;
            int versionCode = context.getPackageManager()
                    .getPackageInfo(pkName, 0).versionCode;
            return pkName + "   " + versionName + "  " + versionCode;
        } catch (Exception e) {

        }
        return null;
    }

    public static String getVersionCode(Context context){
        PackageManager packageManager=context.getPackageManager();
        PackageInfo packageInfo;
        String versionCode="";
        try {
            packageInfo=packageManager.getPackageInfo(context.getPackageName(),0);
            versionCode=packageInfo.versionCode+"";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    //获取屏幕高度
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    //获取屏幕宽度
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    //获取状态栏高度
    public static int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    //dp转px
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    //px转dp
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    //判断是否只有中英文
    public static boolean isName(String name) {
        String str = "^[a-zA-Z\\u4e00-\\u9fa5]+$";
        return Pattern.compile(str).matcher(name).matches();
    }

    //判断是否为手机格式
    public static boolean isPhone(String phone) {
        String check = phone;
        Pattern p = Pattern
                .compile("^((1[0-9]))\\d{9}$");
        Matcher m = p.matcher(check);
        return m.find();
    }

    //判断是否为邮箱格式
    public static boolean isEmail(String email) {
        if (null == email || "".equals(email)) return false;
        Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        Matcher m = p.matcher(email);
        return m.matches();
    }

    //判断是否为邮编格式
    public static boolean isZipNO(String zipString) {
        String str = "^[1-9][0-9]{5}$";
        return Pattern.compile(str).matcher(zipString).matches();
    }

    //验证微信
    public static boolean isWeixin(String weixinString) {
        String str = "^[a-zA-Zd_]{5,}$";
        return Pattern.compile(str).matcher(weixinString).matches();
    }

    //判断字符串是否为空
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    //移除对象
    public static void remove(List<String> data, Object removedata) {
        if (removedata instanceof String) {
            if (data.contains(removedata)) {
                data.remove(removedata);
            }
        }
        if (removedata instanceof List) {
            if (data.containsAll((List<String>) removedata)) {
                data.removeAll((List<String>) removedata);
            }
        }
        if (removedata instanceof HashMap) {
            HashMap<String, List<String>> mapNotChoose = (HashMap<String, List<String>>) removedata;
            List<String> removeKey = new ArrayList<>();
            for (Map.Entry<String, List<String>> entry : mapNotChoose.entrySet()) {
                if (data.contains(entry.getKey())) {
                    removeKey.add(entry.getKey());
                }
            }
            for (String key : removeKey) {
                mapNotChoose.remove(key);
            }
        }
    }

    /**
     * 队列比较
     *
     * @param <T>
     * @param a
     * @param b
     * @return
     */
    public static <T extends Comparable<T>> boolean compare(List<T> a, List<T> b) {
        if (a.size() != b.size())
            return false;
        Collections.sort(a);
        Collections.sort(b);
        for (int i = 0; i < a.size(); i++) {
            if (!a.get(i).equals(b.get(i)))
                return false;
        }
        return true;
    }


    /**
     * 队列比较
     *
     * @param <T>
     * @param a
     * @param b
     * @return
     */
    public static <T extends Comparable<T>> List<String> compareString(List<String> a, List<String> b) {
        List<String> list = new ArrayList<>();
        Collections.sort(a);    //已经是不可选的集合
        Collections.sort(b);    //实体类中的spec1/spec2/spec3
        list.addAll(b);
        for (int i = 0; i < a.size(); i++) {
            if (list.contains(a.get(i))) {
                list.remove(a.get(i));
            }
        }
        return list;
    }

    public static void listAddData(Object list, Object data) {
        if (list instanceof List && list != null && data instanceof String && !CommonUtil.isEmpty(((String) data))) {
            ((List) list).add(data);
        }
    }

    /**
     * 隐藏键盘
     */
    public static void isKeyboard(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    /**
     * 显示键盘
     */
    public static void isKeyTrue(Context context, EditText edit) {
        edit.requestFocus();
//        InputMethodManager imm = (InputMethodManager) edit.getContext().getSystemService(INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
        InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);


    }


    //判断隐藏软键盘是否弹出
    public static boolean isKeybroardShow(Activity activity) {
        if (activity.getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE) {
            return true;
        }
        return false;
    }

    /*
     * 获取系统时间
     */
    public static String getSystemTime() {
        String time;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddhhmmss");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        time = formatter.format(curDate);
        return time;
    }

    /**
     * 实现文本复制功能
     *
     * @param content
     */
    public static void copy(String content, Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setPrimaryClip(ClipData.newPlainText(null, content));

    }

    /**
     * 实现文本复制功能
     */
    public static String getCopy(Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = cmb.getPrimaryClip();
        String resultString = "";
        if (clipData != null) {
            int count = clipData.getItemCount();

            for (int i = 0; i < count; ++i) {
                ClipData.Item item = clipData.getItemAt(i);
                CharSequence str = item
                        .coerceToText(context);

                resultString += str;
            }
        }

        return resultString;
    }

    // 获取当前应用的版本号
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            String version = packInfo.versionName;
            if (!TextUtils.isEmpty(version)) {
                return version;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 防止双击出现重复界面
     *
     * @return
     */
    static long lastClick;

    public static boolean onClick() {
        boolean isClick = false;

        if (System.currentTimeMillis() - lastClick <= 800) {
            isClick = true;
        }
        lastClick = System.currentTimeMillis();
        return isClick;
    }

    /**
     * 防止双击出现重复界面
     *
     * @return
     */
    static long lastClicks;

//    public static void ToastImg(Context context, String string, int img) {
//        View com.wjh.utillibrary.view = View.inflate(context, R.layout.toast_img, null);
//        ImageView iv_img = com.wjh.utillibrary.view.findViewById(R.id.iv_img);
//        iv_img.setImageResource(img);
//        Toast toast = Toast.makeText(context, string, Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.CENTER, 0, 0);
//        toast.setView(com.wjh.utillibrary.view);
//        toast.show();
//    }


    public static void startActivity(Context context, Class<?> cls) {
        Logger.e("跳转类名-----" + cls);
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, Class<?> cls, Bundle bundle) {
        Logger.e("跳转类名-----" + cls);
        Intent intent = new Intent(context, cls);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


    /**
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 将 GCJ-02 坐标转换成 BD-09 坐标
     *
     * @param lat
     * @param lon
     */
    public static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;

    public static double[] gcj02_To_Bd09(double lat, double lon) {
        double x = lon, y = lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
        double tempLon = z * Math.cos(theta) + 0.0065;
        double tempLat = z * Math.sin(theta) + 0.006;
        double[] gps = {tempLat, tempLon};
        return gps;
    }

    public static String encryptByMd5(String src) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            //String result = new String(digest.digest(src.getBytes("utf-8")),"utf-8");
            //Log.e("main", src + "   " + result);
            return MD5.hexdigest(src);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param v
     * @return
     */
    public static Bitmap loadBitmapFromView(View v) {
        int w = v.getWidth();
        int h = v.getHeight();
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        c.drawColor(Color.WHITE);
        c.save();
        v.draw(c);
        c.restore();
        return bmp;
    }



    public static void initUsualWeb(WebView webBase) {
        WebSettings webSettings = webBase.getSettings();

        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setSavePassword(true);
        webSettings.setDomStorageEnabled(true);
        webBase.setSaveEnabled(true);


        webBase.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }


    public static void goToMarket(Context context, String packageName) {
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            if (checkAppInstalled(context, "com.tencent.android.qqdownloader")) {
                goToMarket.setClassName("com.tencent.android.qqdownloader", "com.tencent.pangu.link.LinkProxyActivity");
                context.startActivity(goToMarket);
            } else {
                context.startActivity(goToMarket);
            }
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static boolean checkAppInstalled(Context context, String pkgName) {
        if (pkgName == null || pkgName.isEmpty()) {
            return false;
        }
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> info = packageManager.getInstalledPackages(0);
        if (info == null || info.isEmpty())
            return false;
        for (int i = 0; i < info.size(); i++) {
            if (pkgName.equals(info.get(i).packageName)) {
                return true;
            }
        }
        return false;
    }

    public static void layoutView(ViewGroup src, int width, int height) {
        int widthMeasureSpec = 0;
        int heightMeasureSpec = 0;
        if (width > 0) {
            widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        }
        if (height > 0) {
            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
        }
        src.measure(widthMeasureSpec, heightMeasureSpec);
        src.layout(0, 0, src.getMeasuredWidth(), src.getMeasuredHeight());
    }

    /**
     * Describe：拨打电话
     * Founder：沈东鑫  Time：2018/12/12 21:57  Email：289991233@qq.com
     */
    public static void callPhone(Context context, String phone) {
        context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone)));

    }
}
