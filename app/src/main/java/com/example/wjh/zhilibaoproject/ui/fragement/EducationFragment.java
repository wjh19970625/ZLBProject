package com.example.wjh.zhilibaoproject.ui.fragement;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.example.wjh.zhilibaoproject.R;
import com.example.wjh.zhilibaoproject.adapter.ChildFragmentViewPagerAdapter;
import com.example.wjh.zhilibaoproject.ui.fragement.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class EducationFragment extends BaseFragment {
    private final static String TAG = EducationFragment.class.getSimpleName();
    private TabLayout mToolbarTab;
    private ViewPager mViewPager;
    private List<Fragment> list;
    private String[] data = {"企业","中小学生","科研机构"};

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_education;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mToolbarTab = view.findViewById(R.id.toolbar_tab);
        mViewPager = view.findViewById(R.id.view_pager);
    }

    @Override
    protected void initData() {
        super.initData();
        list = new ArrayList<Fragment>();
        for (int i = 0;i < data.length;i++){
            EducationModeFragment fragment = new EducationModeFragment();
            Bundle bundle = new Bundle();
            bundle.putString("category",data[i]);
            fragment.setArguments(bundle);
            list.add(fragment);
        }

        ChildFragmentViewPagerAdapter adapter = new ChildFragmentViewPagerAdapter(getChildFragmentManager(),list);
        mViewPager.setAdapter(adapter);
        mToolbarTab.setupWithViewPager(mViewPager);
    }
}
