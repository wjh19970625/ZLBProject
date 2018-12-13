package com.example.wjh.zhilibaoproject.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.wjh.zhilibaoproject.R;
import com.example.wjh.zhilibaoproject.adapter.OrdersRecyclerViewAdapter;
import com.example.wjh.zhilibaoproject.bean.GetOneOrdersBean;
import com.example.wjh.zhilibaoproject.network.RetrofitHelper;
import com.example.wjh.zhilibaoproject.network.api.IOrder;
import com.example.wjh.zhilibaoproject.network.base.JsonItem;
import com.example.wjh.zhilibaoproject.network.callback.MsgCallBack;
import com.example.wjh.zhilibaoproject.ui.activity.base.ActionBarActivity;
import com.example.wjh.zhilibaoproject.ui.fragement.EducationModeFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class OrderModeActivity extends ActionBarActivity implements OrdersRecyclerViewAdapter.OnSectionClick{
    private final static String TAG = EducationModeFragment.class.getSimpleName();
    private int state;
    private  int indexPage = 1;
    private RelativeLayout mNoData;
    private RecyclerView mRecyclerView;
    private OrdersRecyclerViewAdapter adapter;
    private SmartRefreshLayout mRefresh;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_mode_order;
    }

    @Override
    public void initView() {
        super.initView();
        Intent intent = getIntent();
        state = intent.getIntExtra("state",9);
        switch (state){
            case 0:
                setCenterTitle("已完成订单");
                break;
            case 1:
                setCenterTitle("未受理订单");
                break;
            case 2:
                setCenterTitle("处理中订单");
                break;
            case 3:
                setCenterTitle("待评价订单");
                break;
            case 4:
                setCenterTitle("未支付订单");
                break;
            case 9:
                setCenterTitle("全部订单");
                break;
        }
        mNoData = (RelativeLayout) findViewById(R.id.no_data);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_mode_order);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRefresh = (SmartRefreshLayout) findViewById(R.id.refresh);
        mRefresh.setRefreshHeader(new ClassicsHeader(this));
        mRefresh.setRefreshFooter(new ClassicsFooter(this));
        mRefresh.setEnableRefresh(true);

        mRefresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                indexPage++;
                if (state == 9){
                    getAllOrders(indexPage);
                } else {
                    getOneOrders(indexPage);
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //初始化页数
                indexPage = 1;
                mRefresh.setEnableLoadMore(true);
                if (state == 9){
                    getAllOrders(indexPage);
                } else {
                    getOneOrders(indexPage);
                }
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        if (state == 9){
            getAllOrders(indexPage);
        } else {
            getOneOrders(indexPage);
        }
    }

    private void getOneOrders(final int indexPage){
        IOrder orderObj = RetrofitHelper.create(IOrder.class);
        orderObj.getMyOneOrders(RetrofitHelper.getBody(new JsonItem("orderState",state),new JsonItem("indexPage",indexPage)))
                .enqueue(new MsgCallBack<GetOneOrdersBean>(OrderModeActivity.this,true) {
                    @Override
                    public void onFailed(Call<GetOneOrdersBean> call, Throwable t) {
                        mRefresh.finishRefresh();
                        mRefresh.finishLoadMore();
                    }
                    @Override
                    public void onSucceed(Call<GetOneOrdersBean> call, Response<GetOneOrdersBean> response) {
                        mRefresh.finishRefresh();
                        mRefresh.finishLoadMore();
                        GetOneOrdersBean.Data[] data = response.body().getData();
                        List<GetOneOrdersBean.Data> list = new ArrayList<GetOneOrdersBean.Data>();
                        for (int i = 0;i < data.length;i++){
                            list.add(data[i]);
                        }
                        Log.e(TAG,"------------>length"+data.length);
                        //当访问页为第一页 且list长度为0时
                        if (indexPage == 1 && list.size() == 0){
                            mNoData.setVisibility(View.VISIBLE);
                            mRefresh.setVisibility(View.GONE);
                        }else {
                            mNoData.setVisibility(View.GONE);
                            mRefresh.setVisibility(View.VISIBLE);
                            if (indexPage > 1){
                                if (list.size() == 0){
                                    mRefresh.setEnableLoadMore(false);
                                } else {
                                    adapter.setData(list);
                                    adapter.notifyDataSetChanged();
                                }
                            }else {
                                adapter = new OrdersRecyclerViewAdapter(list,OrderModeActivity.this);
                                adapter.setOnSectionClick(OrderModeActivity.this);
                            }

                        }
                    }
                });
    }

    private void getAllOrders(final int indexPage){
        IOrder orderObj = RetrofitHelper.create(IOrder.class);
        orderObj.getMyAllOrders(RetrofitHelper.getBody(new JsonItem("indexPage",indexPage)))
                .enqueue(new MsgCallBack<GetOneOrdersBean>(OrderModeActivity.this,true) {
                    @Override
                    public void onFailed(Call<GetOneOrdersBean> call, Throwable t) {
                        mRefresh.finishRefresh();
                        mRefresh.finishLoadMore();
                    }

                    @Override
                    public void onSucceed(Call<GetOneOrdersBean> call, Response<GetOneOrdersBean> response) {
                        mRefresh.finishRefresh();
                        mRefresh.finishLoadMore();
                        GetOneOrdersBean.Data[] data = response.body().getData();
                        Log.e(TAG,"------------>length"+data.length);
                        List<GetOneOrdersBean.Data> list = new ArrayList<GetOneOrdersBean.Data>();
                        for (int i = 0;i < data.length;i++){
                            list.add(data[i]);
                        }
                        //当访问页为第一页 且list长度为0时
                        if (indexPage == 1 && list.size() == 0){
                            mNoData.setVisibility(View.VISIBLE);
                            mRefresh.setVisibility(View.GONE);
                        }else {
                            mNoData.setVisibility(View.GONE);
                            mRefresh.setVisibility(View.VISIBLE);
                            if (indexPage > 1){
                                if (list.size() == 0){
                                    mRefresh.setEnableLoadMore(false);
                                }else {
                                    adapter.setData(list);
                                    adapter.notifyDataSetChanged();
                                }
                            }else {
                                adapter = new OrdersRecyclerViewAdapter(list,OrderModeActivity.this);
                                adapter.setOnSectionClick(OrderModeActivity.this);
                                mRecyclerView.setAdapter(adapter);
                            }
                        }

                    }
                });
    }

    @Override
    public void onSectionClick(String orderId, int position) {
        Intent intent = new Intent();
        intent.setClass(OrderModeActivity.this,PayActivity.class);
        intent.putExtra("orderId",orderId);
        intent.putExtra("state",state);
        startActivity(intent);
    }
}
