package com.example.wjh.zhilibaoproject.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
    private PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;
    private boolean refresh = false;
    OrdersRecyclerViewAdapter adapter;

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
        mPullLoadMoreRecyclerView = (PullLoadMoreRecyclerView) findViewById(R.id.pullLoadMoreRecyclerView);
        mPullLoadMoreRecyclerView.setLinearLayout();
    }

    @Override
    public void initData() {
        super.initData();
        refresh = true;
        switch (state){
            case 0:
                getOneOrders(0,indexPage);
                break;
            case 1:
                getOneOrders(1,indexPage);
                break;
            case 2:
                getOneOrders(2,indexPage);
                break;
            case 3:
                getOneOrders(3,indexPage);
                break;
            case 4:
                getOneOrders(4,indexPage);
                break;
            case 9:
                getAllOrders(indexPage);
                break;
        }
    }

    @Override
    public void init() {
        super.init();
        mPullLoadMoreRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                //初始化页数
                indexPage = 1;
                switch (state){
                    case 0:
                        getOneOrders(0,indexPage);
                        break;
                    case 1:
                        getOneOrders(1,indexPage);
                        break;
                    case 2:
                        getOneOrders(2,indexPage);
                        break;
                    case 3:
                        getOneOrders(3,indexPage);
                        break;
                    case 4:
                        getOneOrders(4,indexPage);
                        break;
                    case 9:
                        getAllOrders(indexPage);
                        break;
                }
            }

            @Override
            public void onLoadMore() {
                indexPage = indexPage + 1;
                refresh = false;
                switch (state){
                    case 0:
                        getOneOrders(0,indexPage);
                        break;
                    case 1:
                        getOneOrders(1,indexPage);
                        break;
                    case 2:
                        getOneOrders(2,indexPage);
                        break;
                    case 3:
                        getOneOrders(3,indexPage);
                        break;
                    case 4:
                        getOneOrders(4,indexPage);
                        break;
                    case 9:
                        getAllOrders(indexPage);
                        break;
                }
            }
        });
    }

    private void getOneOrders(int state, final int indexPage){
        IOrder orderObj = RetrofitHelper.create(IOrder.class);
        orderObj.getMyOneOrders(RetrofitHelper.getBody(new JsonItem("orderState",state),new JsonItem("indexPage",indexPage)))
                .enqueue(new MsgCallBack<GetOneOrdersBean>(OrderModeActivity.this,true) {
                    @Override
                    public void onErrored(Call<GetOneOrdersBean> call, Throwable t) {

                    }

                    @Override
                    public void onSuccessed(Call<GetOneOrdersBean> call, Response<GetOneOrdersBean> response) {
                        GetOneOrdersBean.Data[] data = response.body().getData();
                        List<GetOneOrdersBean.Data> list = new ArrayList<GetOneOrdersBean.Data>();
                        for (int i = 0;i < data.length;i++){
                            list.add(data[i]);
                        }
                        Log.e(TAG,"------------>length"+data.length);
                        //当访问页为第一页 且list长度为0时
                        if (indexPage == 1 && list.size() == 0){
                            mNoData.setVisibility(View.VISIBLE);
                            mPullLoadMoreRecyclerView.setVisibility(View.GONE);
                        }else {
                            if (refresh){
                                adapter = new OrdersRecyclerViewAdapter(list,OrderModeActivity.this);
                                adapter.setOnSectionClick(OrderModeActivity.this);
                                mPullLoadMoreRecyclerView.setAdapter(adapter);
                            }else {
                                adapter.setData(list);
                                adapter.notifyDataSetChanged();
                            }

                            //每次执行完加载或者刷新 关闭
                            mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                        }
                    }
                });
    }

    private void getAllOrders(int indexPage){
        IOrder orderObj = RetrofitHelper.create(IOrder.class);
        orderObj.getMyAllOrders(RetrofitHelper.getBody(new JsonItem("indexPage",indexPage)))
                .enqueue(new MsgCallBack<GetOneOrdersBean>(OrderModeActivity.this,true) {
                    @Override
                    public void onErrored(Call<GetOneOrdersBean> call, Throwable t) {

                    }

                    @Override
                    public void onSuccessed(Call<GetOneOrdersBean> call, Response<GetOneOrdersBean> response) {
                        GetOneOrdersBean.Data[] data = response.body().getData();
                        Log.e(TAG,"------------>length"+data.length);
                        List<GetOneOrdersBean.Data> list = new ArrayList<GetOneOrdersBean.Data>();
                        for (int i = 0;i < data.length;i++){
                            list.add(data[i]);
                        }
                        if (refresh){
                            adapter = new OrdersRecyclerViewAdapter(list,OrderModeActivity.this);
                            adapter.setOnSectionClick(OrderModeActivity.this);
                            mPullLoadMoreRecyclerView.setAdapter(adapter);
                        }else {
                            adapter.setData(list);
                            adapter.notifyDataSetChanged();
                        }

                        //每次执行完加载或者刷新 关闭
                        mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();

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
