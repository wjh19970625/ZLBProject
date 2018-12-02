package com.example.wjh.zhilibaoproject.ui.activity;

import com.example.wjh.zhilibaoproject.R;
import com.example.wjh.zhilibaoproject.ui.activity.base.ActionBarActivity;

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
