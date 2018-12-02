package com.example.wjh.zhilibaoproject.ui.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.wjh.zhilibaoproject.R;
import com.example.wjh.zhilibaoproject.adapter.FragmentViewPagerAdapter;
import com.example.wjh.zhilibaoproject.ui.activity.base.WidgetActivity;
import com.example.wjh.zhilibaoproject.ui.fragement.EducationFragment;
import com.example.wjh.zhilibaoproject.ui.fragement.IndexFragment;
import com.example.wjh.zhilibaoproject.ui.fragement.MarketFragment;
import com.example.wjh.zhilibaoproject.ui.fragement.MeFragment;
import com.example.wjh.zhilibaoproject.utils.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import crossoverone.statuslib.StatusUtil;

public class MainActivity extends WidgetActivity {
    private NoScrollViewPager mAty_main_vp;
    private RadioGroup mItem_rg;
    private List<Fragment> list;
//    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 10000;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        setSwipeBackEnable(false);
        mAty_main_vp = (NoScrollViewPager) findViewById(R.id.aty_main_vp);
        mItem_rg = (RadioGroup) findViewById(R.id.item_rg);

        //设置ViewPager不能滑动
        mAty_main_vp.setNoScroll(true);
    }

    @Override
    public void initData() {
        list = new ArrayList<Fragment>();
        list.add(new IndexFragment());
        list.add(new EducationFragment());
        list.add(new MarketFragment());
        list.add(new MeFragment());

        FragmentViewPagerAdapter adapter = new FragmentViewPagerAdapter(getSupportFragmentManager(),list);
        mAty_main_vp.setAdapter(adapter);
        mAty_main_vp.setOffscreenPageLimit(3);
    }

    @Override
    public void init() {
        mItem_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.index_rb:
                        mAty_main_vp.setCurrentItem(0);
                        break;
                    case R.id.education_rb:
                        mAty_main_vp.setCurrentItem(1);
                        break;
                    case R.id.market_rb:
                        mAty_main_vp.setCurrentItem(2);
                        break;
                    case R.id.me_rb:
                        mAty_main_vp.setCurrentItem(3);
                        break;
                }
            }
        });
    }

    private void checkPermission() {
        //检查权限（NEED_PERMISSION）是否被授权 PackageManager.PERMISSION_GRANTED表示同意授权
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //用户已经拒绝过一次，再次弹出权限申请对话框需要给用户一个解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
                    .WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "请开通相关权限，否则无法正常使用本应用！", Toast.LENGTH_SHORT).show();
            }
            //申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            //用户已经拒绝过一次，再次弹出权限申请对话框需要给用户一个解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
                    .CALL_PHONE)) {
                Toast.makeText(this, "请开通相关权限，否则无法正常使用本应用！", Toast.LENGTH_SHORT).show();
            }
            //申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);

        } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            //用户已经拒绝过一次，再次弹出权限申请对话框需要给用户一个解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
                    .ACCESS_NETWORK_STATE)) {
                Toast.makeText(this, "请开通相关权限，否则无法正常使用本应用！", Toast.LENGTH_SHORT).show();
            }
            //申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 1);

        } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            //用户已经拒绝过一次，再次弹出权限申请对话框需要给用户一个解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
                    .READ_PHONE_STATE)) {
                Toast.makeText(this, "请开通相关权限，否则无法正常使用本应用！", Toast.LENGTH_SHORT).show();
            }
            //申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);

        } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //用户已经拒绝过一次，再次弹出权限申请对话框需要给用户一个解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
                    .WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "请开通相关权限，否则无法正常使用本应用！", Toast.LENGTH_SHORT).show();
            }
            //申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        }else {
            Log.e("MainActivity", "checkPermission: 已经授权！");
        }
    }

    @Override
    protected void setStatusColor() {
        StatusUtil.setUseStatusBarColor(this, Color.TRANSPARENT);
    }

    @Override
    protected void setSystemInvadeBlack() {
        // 第二个参数是是否沉浸,第三个参数是状态栏字体是否为黑色。
        StatusUtil.setSystemStatus(this, true, false);
    }

}
