package com.example.zlb.api;

import com.example.zlb.bean.CallBackBaseBean;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface IIndex {
    //意见反馈
    @POST("/index/suggest.do")
    Call<CallBackBaseBean> suggest(@QueryMap Map<String, String> map);
    //检查更新
    @GET("/version/checkVersion.do")
    Call<CallBackBaseBean> checkVersion(@QueryMap Map<String, String> map);
}
