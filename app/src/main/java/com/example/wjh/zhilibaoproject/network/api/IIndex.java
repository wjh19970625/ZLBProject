package com.example.wjh.zhilibaoproject.network.api;

import com.example.wjh.zhilibaoproject.bean.CallBackBaseBean;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface IIndex {
    //意见反馈
    @POST("/index/suggest.do")
    Call<CallBackBaseBean> suggest(@QueryMap Map<String, String> map);
}
