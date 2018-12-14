package com.example.wjh.zlb.ui.activity;

import com.example.wjh.zlb.R;
import com.example.wjh.zlb.ui.activity.base.ActionBarActivity;

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
