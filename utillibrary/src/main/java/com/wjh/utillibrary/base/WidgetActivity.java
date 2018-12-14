package com.wjh.utillibrary.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by wjh on 2018/6/25.
 */

public abstract class WidgetActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLayout();
        initView();
        initData();
        init();
    }

    protected abstract int getLayoutId();

    public void initLayout(){
        setContentView(getLayoutId());
    }

    public void initView(){

    }

    public void init(){

    }

    public void initData(){

    }


    protected boolean isStatusBar() {
        return true;
    }

}
