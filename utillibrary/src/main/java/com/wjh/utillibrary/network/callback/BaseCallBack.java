package com.wjh.utillibrary.network.callback;

import android.content.Context;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.wjh.utillibrary.utils.Logger;

/**
 * Created by wjh on 2018/6/28.
 */

public abstract class BaseCallBack<T> implements Callback<T> {
    Context context;
    long startTime = System.currentTimeMillis();
    public BaseCallBack(Context context){
        this.context = context;
    }
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        String result = response.toString();
        Logger.e("   \n");
        Logger.e("---------------------Request Log Start---------------------");
        Logger.e("| Response:" + result);
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        Logger.e("---------------------Request Log End:" + duration + "毫秒---------------------");
        Logger.e("   \n");

        T body = response.body();
        if (body == null){
            int code = response.code();
            if (code == 400){
                onError(call,new Throwable("请求失败"));
            }else if (response != null && code == 401){
                onError(call,new Throwable("登录失效"),401);
                //401表示用户登录信息失效，需要跳转到登录界面重新登录
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
        Logger.e("http-code------>"+code);
    }
    protected abstract void onError(Call<T> call,Throwable t);
    public abstract void onSuccess(Call<T> call,Response<T> response);
}
