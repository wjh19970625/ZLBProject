package com.example.zlb.ui.fragement;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.zlb.R;
import com.example.zlb.adapter.OrdersRecyclerViewAdapter;
import com.example.zlb.api.IOrder;
import com.example.zlb.bean.GetOneOrdersBean;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.wjh.utillibrary.base.BaseFragment;
import com.wjh.utillibrary.network.RetrofitHelper;
import com.wjh.utillibrary.network.base.JsonItem;
import com.wjh.utillibrary.network.callback.MsgCallBack;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class OrderFragment extends BaseFragment {
    private final static String TAG = OrderFragment.class.getSimpleName();
    private String state;
    private  int indexPage = 1;
    private RelativeLayout mNoData;
    private RecyclerView mRecyclerView;
    private OrdersRecyclerViewAdapter adapter;
    private SmartRefreshLayout mRefresh;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_order;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        if (getArguments() != null) {
            state = getArguments().getString("state");
        }

        mNoData = view.findViewById(R.id.no_data);
        mRecyclerView = view.findViewById(R.id.rv_mode_order);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OrdersRecyclerViewAdapter(getContext(), null);
        mRecyclerView.setAdapter(adapter);
        mRefresh = view.findViewById(R.id.refresh);
        mRefresh.setRefreshHeader(new ClassicsHeader(getContext()));
        mRefresh.setRefreshFooter(new ClassicsFooter(getContext()));
        mRefresh.setEnableRefresh(true);

        mRefresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                indexPage++;
                requestData();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //初始化页数
                indexPage = 1;
                mRefresh.setEnableLoadMore(true);
                requestData();
            }
        });
    }

    private void requestData(){
        if (state.equals("9")){
            getAllOrders(indexPage);
        } else {
            getOneOrders(indexPage);
        }
    }

    @Override
    protected void initData() {
        super.initData();
        mRefresh.autoRefresh();
    }

    private void getOneOrders(final int indexPage){
        IOrder orderObj = RetrofitHelper.create(IOrder.class);
        orderObj.getMyOneOrders(RetrofitHelper.getBody(new JsonItem("orderState",state),new JsonItem("indexPage",indexPage)))
                .enqueue(new MsgCallBack<GetOneOrdersBean>(getContext(),false) {
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
                                    adapter.addData(list);
                                }
                            }else {
                                adapter.setNewData(list);
                            }
                        }
                    }
                });
    }

    private void getAllOrders(final int indexPage){
        IOrder orderObj = RetrofitHelper.create(IOrder.class);
        orderObj.getMyAllOrders(RetrofitHelper.getBody(new JsonItem("indexPage",indexPage)))
                .enqueue(new MsgCallBack<GetOneOrdersBean>(getContext(),false) {
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
                                    adapter.addData(list);
                                }
                            }else {
                                adapter.setNewData(list);
                            }
                        }
                    }
                });
    }

}


