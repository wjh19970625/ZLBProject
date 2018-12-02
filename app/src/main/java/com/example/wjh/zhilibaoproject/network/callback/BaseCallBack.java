package com.example.wjh.zhilibaoproject.network.callback;

import android.content.Context;
import android.content.Intent;

import com.example.wjh.zhilibaoproject.ui.activity.LoginActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by wjh on 2018/6/28.
 */

public abstract class BaseCallBack<T> implements Callback<T> {
    Context context;
    public BaseCallBack(Context context){
        this.context = context;
    }
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        T body = response.body();
        if (body == null){
            int code = response.code();
            if (code == 400){
                onError(call,new Throwable("请求失败"));
            }else if (response != null && code == 401){
                onError(call,new Throwable(""),401);
                //401表示用户登录信息失效，需要跳转到登录界面重新登录
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
            }else if (response != null && code == 500){
                onError(call,new Throwable("服务器错误"));
            }else {
                onError(call,new Throwable("未知错误"));
            }
        }else{
            //终于成功了
            onSuccess(call,response);
        }
    }

    @Override
    public void onFailure(Call call, Throwable t) {
        onError(call,t);
    }

    protected  void onError(Call<T> call,Throwable t,int code){
        onError(call,t);
    }
    protected abstract void onError(Call<T> call,Throwable t);
    public abstract void onSuccess(Call<T> call,Response<T> response);
}
