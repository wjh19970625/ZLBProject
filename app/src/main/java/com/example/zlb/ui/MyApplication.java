package com.example.zlb.ui;

import android.app.Application;

import com.awen.photo.FrescoImageLoader;

import com.wjh.utillibrary.utils.Logger;
import com.wjh.utillibrary.utils.UserInfoHelper;

public class MyApplication extends Application {
    private static MyApplication application;
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        FrescoImageLoader.init(this);
        Logger.setDebug(true);
        UserInfoHelper.init(this);
    }

    public static MyApplication application(){
        return application;
    }
}
