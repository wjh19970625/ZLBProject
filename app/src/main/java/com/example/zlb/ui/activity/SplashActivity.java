package com.example.zlb.ui.activity;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import com.example.zlb.R;

import com.wjh.utillibrary.base.WidgetActivity;
import com.wjh.utillibrary.common.Config;
import crossoverone.statuslib.StatusUtil;

public class SplashActivity extends WidgetActivity {
    class SplashHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == Config.SPLASH_CODE){
                startActivity(MainActivity.class);
                finish();
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //解决启动屏问题
        if (!isTaskRoot()) {
            finish();
            return;
        }
        setSwipeBackEnable(false);
        SplashHandler handler = new SplashHandler();
        handler.sendEmptyMessageDelayed(Config.SPLASH_CODE,3000);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void setStatusColor() {
        StatusUtil.setUseStatusBarColor(this, Color.TRANSPARENT);
    }

    @Override
    protected void setSystemInvadeBlack() {
        // 第二个参数是是否沉浸,第三个参数是状态栏字体是否为黑色。
        StatusUtil.setSystemStatus(this, true, true);
    }

}
