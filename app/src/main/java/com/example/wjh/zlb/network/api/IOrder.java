package com.example.wjh.zlb.network.api;

import com.example.wjh.zlb.bean.CreateOrdersBean;
import com.example.wjh.zlb.bean.GetOneOrdersBean;
import com.example.wjh.zlb.bean.GetOrderDetailBean;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface IOrder {
    //获取全部订单
    @GET("/order/get_my_all_orders.do")
    Call<GetOneOrdersBean> getMyAllOrders(@QueryMap Map<String, String> map);

    //获取个别订单 0 订单已受理 1 订单未受理 2 订单处理中 3 待评价订单
    @GET("/order/get_orders_by_state.do")
    Call<GetOneOrdersBean> getMyOneOrders(@QueryMap Map<String, String> map);

    //创建订单
    @POST("/order/create_order.do")
    Call<CreateOrdersBean> createOrder(@QueryMap Map<String, String> map);

    //获取订单详情
    @GET("/order/get_order_details.do")
    Call<GetOrderDetailBean> getOrderDetails(@QueryMap Map<String, String> map);

}
