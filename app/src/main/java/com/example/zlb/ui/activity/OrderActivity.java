package com.example.zlb.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.example.zlb.R;
import com.example.zlb.ui.fragement.OrderFragment;
import com.wjh.utillibrary.base.ActionBarActivity;
import com.wjh.utillibrary.utils.CommonUtil;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends ActionBarActivity {
    private List<OrderFragment> fragmentList = new ArrayList<>();
    private List<String> fragmentTitle = new ArrayList<>();
    private ViewPager mViewPager;
    private int index = 0;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_order;
    }

    @Override
    public void initView() {
        super.initView();
        setCenterTitle("我的订单");
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
    }

    @Override
    public void initData() {
        super.initData();
        Intent intent = getIntent();
        index = intent.getIntExtra("index",0);
        initFragment();
    }

    private void initFragment(){
        List<String> state = new ArrayList<>();
        fragmentTitle.add("全部");
        state.add("9");
        fragmentTitle.add("未支付");
        state.add("4");
        fragmentTitle.add("未受理");
        state.add("1");
        fragmentTitle.add("处理中");
        state.add("2");
        fragmentTitle.add("待评价");
        state.add("3");
        fragmentTitle.add("已完成");
        state.add("0");

        for (int i = 0; i < fragmentTitle.size(); i++) {
            Bundle bundle = new Bundle();
            bundle.putString("state", state.get(i));
            OrderFragment orderFragment = new OrderFragment();
            orderFragment.setArguments(bundle);
            fragmentList.add(orderFragment);
        }

        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager(), fragmentList, fragmentTitle);
        mViewPager.setAdapter(tabAdapter);
        initMagicIndicator();
        mViewPager.setCurrentItem(index);

    }

    private void initMagicIndicator() {
        MagicIndicator magicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator);
        magicIndicator.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(fragmentTitle.size() <= 4);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return fragmentTitle.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setNormalColor(ContextCompat.getColor(context, R.color.black));
                simplePagerTitleView.setSelectedColor(ContextCompat.getColor(context, R.color.blue));
                simplePagerTitleView.setText(fragmentTitle.get(index));
                simplePagerTitleView.setTextSize(16);

                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                linePagerIndicator.setLineHeight(CommonUtil.dip2px(context, 2));
                linePagerIndicator.setColors(ContextCompat.getColor(context, R.color.blue));

                return linePagerIndicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, mViewPager);
    }

    class TabAdapter extends FragmentStatePagerAdapter {
        private List<OrderFragment> fragmentList;
        private List<String> mList;

        public TabAdapter(FragmentManager fm, List<OrderFragment> fragmentList, List<String> mList) {
            super(fm);
            this.fragmentList = fragmentList;
            this.mList = mList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mList.get(position);
        }

    }
}
