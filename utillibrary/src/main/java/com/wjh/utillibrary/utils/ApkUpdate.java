package com.wjh.utillibrary.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;

import com.yanzhenjie.permission.AndPermission;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApkUpdate {
    private Context mContext;
    private ProgressDialog pd = null;
    private String UPDATE_SERVICE_APK = "ApkAndroid.apk";
    private boolean isDownLoad = true;
    private String url;
    private AlertDialog.Builder builder = null;

    public ApkUpdate(Context context,String url){
        this.mContext = context;
        this.url = url;
    }

    public void doUpdate(){
        pd = new ProgressDialog(mContext);
        pd.setTitle("");
        pd.setMessage("正在更新,请稍等");
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(false);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        //下载路径
        downFile(url);
    }


    /**
     * 下载apk
     */
    public void downFile(final String url) {
        pd.show();
        new Thread() {
            public void run() {
                try {
                    HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                    long length = conn.getContentLength();
                    pd.setMax((int) length / 1024);
                    InputStream is = (InputStream) conn.getContent();
                    FileOutputStream fileOutputStream = null;
                    if (is != null) {
                        File file = new File(Environment.getExternalStorageDirectory(), UPDATE_SERVICE_APK);
                        fileOutputStream = new FileOutputStream(file);
                        byte[] b = new byte[1024];
                        int charb = -1;
                        int count = 0;
                        while ((charb = is.read(b)) != -1 && isDownLoad) {
                            fileOutputStream.write(b, 0, charb);
                            count += charb / 1024;
                            //当前下载量
                            pd.setProgress(count);
                        }
                    }
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    down();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            pd.cancel();
            update();
        }
    };

    /**
     * 下载完成，通过handler将下载对话框取消
     */
    public void down() {
        new Thread() {
            public void run() {
                Message message = handler.obtainMessage();
                handler.sendMessage(message);
            }
        }.start();
    }

    /**
     * 安装应用
     */
    public void update() {
        AndPermission.with(mContext).install().file(new File(Environment.getExternalStorageDirectory(), UPDATE_SERVICE_APK)).start();
    }
}
