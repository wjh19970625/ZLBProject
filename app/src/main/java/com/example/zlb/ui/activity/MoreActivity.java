package com.example.zlb.ui.activity;

import com.example.zlb.R;

import com.wjh.utillibrary.base.ActionBarActivity;

public class MoreActivity extends ActionBarActivity {
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_more;
    }

    @Override
    public void initView() {
        super.initView();
        setCenterTitle("更多");
    }

}
