package com.example.zlb.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.zlb.R;
import com.example.zlb.adapter.FragmentViewPagerAdapter;
import com.example.zlb.api.IIndex;
import com.example.zlb.bean.CallBackBaseBean;
import com.example.zlb.ui.fragement.EducationFragment;
import com.example.zlb.ui.fragement.IndexFragment;
import com.example.zlb.ui.fragement.MarketFragment;
import com.example.zlb.ui.fragement.MeFragment;

import java.util.ArrayList;
import java.util.List;

import com.wjh.utillibrary.base.WidgetActivity;
import crossoverone.statuslib.StatusUtil;
import retrofit2.Call;
import retrofit2.Response;

import com.wjh.utillibrary.network.RetrofitHelper;
import com.wjh.utillibrary.network.base.JsonItem;
import com.wjh.utillibrary.network.callback.MsgCallBack;
import com.wjh.utillibrary.utils.ApkUpdate;
import com.wjh.utillibrary.utils.CommonUtil;
import com.wjh.utillibrary.view.NoScrollViewPager;
import com.wjh.utillibrary.view.dialog.DialogTool;

import static com.wjh.utillibrary.common.Config.SERVICE_URL;

public class MainActivity extends WidgetActivity {
    private NoScrollViewPager mAty_main_vp;
    private RadioGroup mItem_rg;
    private List<Fragment> list;
    private static final int REQUEST_CODE_APP_INSTALL = 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            boolean hasInstallPermission = isHasInstallPermissionWithO(mContext);
            if (!hasInstallPermission) {
                startInstallPermissionSettingActivity(mContext);
                return;
            } else {
                initApk();
            }
        }else {
            initApk();
        }
    }

    private void initApk(){
        int versionNum = Integer.parseInt(CommonUtil.getVersionCode(this));
        IIndex indexOb = RetrofitHelper.create(IIndex.class);
        indexOb.checkVersion(RetrofitHelper.getBody(new JsonItem("versionNum",versionNum)))
                .enqueue(new MsgCallBack<CallBackBaseBean>(this,true) {
                    @Override
                    public void onFailed(Call<CallBackBaseBean> call, Throwable t) {

                    }

                    @Override
                    public void onSucceed(Call<CallBackBaseBean> call, Response<CallBackBaseBean> response) {
                        int status = response.body().getStatus();
                        final String msg = response.body().getMsg();
                        if (status == 0){
                            DialogTool dialogTool = new DialogTool(MainActivity.this);
                            dialogTool.dialogShow("发现新版本，是否更新");
                            dialogTool.setOnDialogClickListener(new DialogTool.OnDialogClickListener() {
                                @Override
                                public void onDialogOkClick() {
                                    ApkUpdate apkUpdate = new ApkUpdate(MainActivity.this,SERVICE_URL + msg);
                                    apkUpdate.doUpdate();
                                }

                                @Override
                                public void onDialogCancelClick() {

                                }
                            });
                        } else {
                            showToast(msg);
                        }
                    }
                });
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
                showToast("请开通相关权限，否则无法正常使用本应用！");
            }
            //申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
                    .CALL_PHONE)) {
                showToast("请开通相关权限，否则无法正常使用本应用！");
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);

        } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
                    .ACCESS_NETWORK_STATE)) {
                showToast("请开通相关权限，否则无法正常使用本应用！");
            }
            //申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 1);

        } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
                    .READ_PHONE_STATE)) {
                showToast("请开通相关权限，否则无法正常使用本应用！");
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);

        } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
                    .WRITE_EXTERNAL_STORAGE)) {
                showToast("请开通相关权限，否则无法正常使用本应用！");
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        }else {
            Log.e("MainActivity", "checkPermission: 已经授权！");
        }
    }

    /**
     * 开启设置安装未知来源应用权限界面
     * @param context
     */
    @RequiresApi (api = Build.VERSION_CODES.O)
    private void startInstallPermissionSettingActivity(Context context) {
        if (context == null){
            return;
        }
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        ((Activity)context).startActivityForResult(intent,REQUEST_CODE_APP_INSTALL);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean isHasInstallPermissionWithO(Context context){
        if (context == null){
            return false;
        }
        return context.getPackageManager().canRequestPackageInstalls();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_APP_INSTALL){
            if (resultCode == RESULT_OK){
                initApk();
            }
        }
    }
}
