package com.example.zlb.api;

import com.example.zlb.bean.CallBackBaseBean;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface IIndex {
    //意见反馈
    @POST("/index/suggest.do")
    Call<CallBackBaseBean> suggest(@QueryMap Map<String, String> map);
}
