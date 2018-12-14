package com.example.wjh.zlb.ui;

import android.app.Application;

import com.awen.photo.FrescoImageLoader;
import com.example.wjh.zlb.utils.Logger;

public class MyApplication extends Application {
    private static MyApplication application;
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        FrescoImageLoader.init(this);
        Logger.setDebug(true);
    }

    public static MyApplication application(){
        return application;
    }
}