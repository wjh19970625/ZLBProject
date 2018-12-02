package com.example.wjh.zhilibaoproject.network.api;

import com.example.wjh.zhilibaoproject.bean.MarketDetailBean;
import com.example.wjh.zhilibaoproject.bean.MarketListBean;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface IMarket {
    @GET("/market/get_market_list.do")
    Call<MarketListBean>getMarketList();

    @GET("/market/get_idea_details.do")
    Call<MarketDetailBean>getIdeaDetails(@QueryMap Map<String, String> map);
}
