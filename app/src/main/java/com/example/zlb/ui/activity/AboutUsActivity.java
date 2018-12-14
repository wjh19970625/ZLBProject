package com.example.zlb.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.example.zlb.R;

import base.ActionBarActivity;
import utils.Utils;


public class AboutUsActivity extends ActionBarActivity {

    private TextView mContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCenterTitle("关于我们");
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_about_us;
    }

    @Override
    public void initView() {
        super.initView();
        mContent = (TextView) findViewById(R.id.content);
    }

    @Override
    public void initData() {
        super.initData();
        String data = "     知利宝知识产权服务互联网平台项目于2018年 2月启动,是一家专注为企事业单位、个人、" +
                "中小学生及政府公共服务平台提供在线知识产权代理及交易服务、法律服务、知识产权教育及创新创" +
                "意思维成果转化等全方位知识产权互联网创新型平台，开创互联网+知识产权的全新模式，对知识产权" +
                "行业的发展和变革具有重要意义。";
        String content = Utils.ToDBC(data);
        mContent.setText(content);
    }

}
