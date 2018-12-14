package com.wjh.utillibrary.base;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.wjh.utillibrary.R;


/**
 * Created by Administrator on 2018/5/8.
 */

public abstract class ActionBarFragment extends BaseFragment {
    private Toolbar mCenterToolbar;
    private FrameLayout mContentLayout;
    private TextView mCenterTitle;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_base_actionbar;
    }

    protected abstract int getContentLayoutId();

    @Override
    protected void initView(View view) {
        mCenterToolbar = view.findViewById(R.id.fra_action_bar_center_toolbar);
        mCenterTitle = view.findViewById(R.id.fra_action_text_center_title);
        mContentLayout = view.findViewById(R.id.fra_action_layout_content_layout);
        View contentView = LayoutInflater.from(getContext()).inflate(getContentLayoutId(),null,false);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mCenterToolbar);
        mContentLayout.addView(contentView);
        mCenterToolbar.setTitle("");
        mCenterToolbar.setSubtitle("");
        getActivity().setTitle("");
    }

    /*设置title*/
    public void setCenterTitle(CharSequence msg){
        mCenterTitle.setText(msg);
    }
}
