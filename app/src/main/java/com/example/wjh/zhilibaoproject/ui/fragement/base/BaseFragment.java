package com.example.wjh.zhilibaoproject.ui.fragement.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by Administrator on 2018/5/8.
 */

public abstract class BaseFragment extends Fragment {
    ProgressDialog progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(),null,false);
        if (view == null){
            view = new View(getContext());
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        init();
        initData();
    }

    protected void initView(View view){

    }

    protected void init(){

    }

    protected void initData(){

    }

    /*Toast提示*/
    public void showToast(String msg){
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void showToast(int res){
        Toast.makeText(getContext(), res, Toast.LENGTH_SHORT).show();
    }

    /*对话框*/
    public void showProgressDialog(){
        if (null == progressDialog){
            progressDialog = new ProgressDialog(getContext());
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
        intent.setClass(getContext(),cls);
        startActivity(intent);
    }

    public void startActivityForResult(Class<? extends Activity> cls,int request){
        Intent intent = new Intent();
        intent.setClass(getContext(),cls);
        startActivityForResult(intent,request);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        closedProgressDialog();
    }

    protected abstract int getLayoutId();
}
