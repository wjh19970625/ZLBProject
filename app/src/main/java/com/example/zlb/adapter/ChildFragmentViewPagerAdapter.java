package com.example.zlb.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by wjh on 2018/6/30.
 */

public class ChildFragmentViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> list;
    private String [] title = {"企业","中小学生","科研机构"};
    public ChildFragmentViewPagerAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }

}
