package com.example.wjh.zhilibaoproject.network.callback;

import android.content.Context;

import com.example.wjh.zhilibaoproject.ui.activity.base.BaseActivity;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Administrator on 2018/6/28.
 */

public  abstract class MsgCallBack<T> extends BaseCallBack<T> {
    public MsgCallBack(Context context) {
        super(context);
    }

    public MsgCallBack(Context context,boolean isShowProgress) {
        super(context);
        if (isShowProgress){
            showProgressDialog();
        }
    }

    private void showProgressDialog(){
        if (context != null)
            ((BaseActivity)context).showProgressDialog();
    }

    @Override
    protected void onError(Call<T> call, Throwable t) {
        ((BaseActivity)context).showToast(t.getMessage());
        ((BaseActivity)context).closedProgressDialog();
        onErrored(call,t);
    }

    @Override
    public void onSuccess(Call<T> call, Response<T> response) {
        ((BaseActivity)context).closedProgressDialog();
        onSuccessed(call,response);
    }

    public abstract void onErrored(Call<T> call, Throwable t);
    public abstract void onSuccessed(Call<T> call,Response<T> response);

}
