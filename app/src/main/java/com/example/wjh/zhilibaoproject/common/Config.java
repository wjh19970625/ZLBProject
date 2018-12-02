package com.example.wjh.zhilibaoproject.common;

import android.os.Environment;

import java.io.File;

public class Config {
    //y1857e9044.iask.in:21909测试
    //120.24.56.46:10001
    public static final String SERVICE_URL = "https://www.zlbipr.com:443";

//    public static final String SERVICE_URL = "http://y1857e9044.iask.in:21909";

    public static final int SPLASH_CODE = 10000;

    public static final String STORAGE_PATH = Environment.getExternalStorageDirectory().getPath()+
            File.separator+"ZhiLiBao"+File.separator;

    public static final String UPLOAD_IMAGE_NAME="uploadImage.jpg";
}
