package com.wjh.utillibrary.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import crossoverone.statuslib.StatusUtil;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by wjh on 2018/6/25.
 */

public abstract class BaseActivity extends SwipeBackActivity {
    ProgressDialog progressDialog;
    private boolean isDestroyed = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusColor();
        setSystemInvadeBlack();
    }

    /*吐司提示*/
    public void showToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }

    public void showToast(int msg){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }

    /*对话框*/
    public void showProgressDialog(){
        if (null == progressDialog){
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
        }
        if (progressDialog.isShowing()){
            return;
        }else{
            progressDialog.show();
        }
        progressDialog.setMessage("正在加载");
    }

    public void showProgressDialog(String msg){
        showProgressDialog();
        progressDialog.setMessage(msg);
    }
    public void closedProgressDialog(){
        if (null != progressDialog && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    /*跳转*/
    public void startActivity(Class<? extends Activity> cls){
        Intent intent = new Intent();
        intent.setClass(this,cls);
        startActivity(intent);
    }

    public void startActivityForResult(Class<? extends Activity> cls,int request){
        Intent intent = new Intent();
        intent.setClass(this,cls);
        startActivityForResult(intent,request);
    }

    public BaseActivity getThis(){
        return this;
    }

    protected void setStatusColor(){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
//            StatusUtil.setUseStatusBarColor(this, Color.TRANSPARENT,Color.parseColor("#219ff0"));
//        }else{
//            StatusUtil.setUseStatusBarColor(this,Color.parseColor("#219ff0"));
//        }

        StatusUtil.setUseStatusBarColor(this, Color.TRANSPARENT);
    }

    protected void setSystemInvadeBlack(){
        // 第二个参数是是否沉浸,第三个参数是状态栏字体是否为黑色。
        StatusUtil.setSystemStatus(this, true, true);

    }
    /*Destroy*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        closedProgressDialog();
        isDestroyed = true;
    }

    public boolean isDestroyed(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            return super.isDestroyed();
        }
        return isDestroyed;
    }

}
