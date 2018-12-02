package com.example.wjh.zhilibaoproject.ui;

import android.app.Application;

import com.awen.photo.FrescoImageLoader;

public class MyApplication extends Application {
    private static MyApplication application;
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        FrescoImageLoader.init(this);
    }

    public static MyApplication application(){
        return application;
    }
}
