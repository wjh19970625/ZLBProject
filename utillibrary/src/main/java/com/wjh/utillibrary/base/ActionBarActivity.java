package com.wjh.utillibrary.base;

import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.wjh.utillibrary.R;

/**
 * Created by wjh on 2018/6/25.
 */

public abstract class ActionBarActivity extends WidgetActivity {
    private Toolbar mCenterToolbar;
    private FrameLayout mContentLayout;
    private TextView mCenterTitle;
    private View mContentView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_base_actionbar;
    }

    protected abstract int getContentLayoutId();

    @Override
    public void initView() {
        super.initView();
        mCenterToolbar = (Toolbar) findViewById(R.id.aty_action_bar_center_toolbar);
        mContentLayout = (FrameLayout) findViewById(R.id.aty_action_layout_content_layout);
        mCenterTitle = (TextView) findViewById(R.id.aty_action_text_center_title);
        mContentView = LayoutInflater
                .from(this)
                .inflate(getContentLayoutId(),mContentLayout,false);
        if (mContentView == null){
            mContentView = mContentLayout;
        }else {
            mContentLayout.addView(mContentView);
        }

        setSupportActionBar(mCenterToolbar);
        mCenterToolbar.setNavigationIcon(R.mipmap.white_back);
        mCenterToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onToolBarBackPressed();
            }
        });
        mCenterToolbar.setTitle("");
        mCenterToolbar.setSubtitle("");
        setTitle("");
    }

    /*Toolbar退后事件*/
    public void onToolBarBackPressed(){
        finish();
    }

    /*设置title*/
    public void setCenterTitle(CharSequence msg){
        mCenterTitle.setText(msg);
    }
}
