package com.example.wjh.zlb.network.api;

import com.example.wjh.zlb.bean.CallBackBaseBean;
import com.example.wjh.zlb.bean.ProductDetailBean;
import com.example.wjh.zlb.bean.ProductListBean;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface IProduct {
    //服务商品列表
    @GET("/admin/product/get_product_list.do")
    Call<ProductListBean> getProductList();

    //获取服务商品信息
    @GET("/product/get_product_info.do")
    Call<ProductDetailBean> getProductInfo(@QueryMap Map<String, String> map);

    //支付订单
    @POST("/mobile/pay.do")
    Call<CallBackBaseBean> pay(@QueryMap Map<String, String> map);
}
