package com.example.wjh.zhilibaoproject.ui.fragement;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.example.wjh.zhilibaoproject.R;
import com.example.wjh.zhilibaoproject.adapter.MarketAdapter;
import com.example.wjh.zhilibaoproject.bean.MarketListBean;
import com.example.wjh.zhilibaoproject.network.RetrofitHelper;
import com.example.wjh.zhilibaoproject.network.api.IMarket;
import com.example.wjh.zhilibaoproject.network.callback.MsgCallBack;
import com.example.wjh.zhilibaoproject.ui.activity.MarketDetailModeActivity;
import com.example.wjh.zhilibaoproject.ui.fragement.base.BaseFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class MarketFragment extends BaseFragment implements MarketAdapter.OnSectionClick {
    private RecyclerView mRecyclerView;
    private MarketAdapter mAdapter;
    private SmartRefreshLayout mRefresh;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_market;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mRecyclerView = view.findViewById(R.id.rv_market);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));//设置瀑布流
        mRefresh = view.findViewById(R.id.refresh);
        mRefresh.setRefreshHeader(new ClassicsHeader(getContext()));
        mRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getData();
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        getData();

    }

    private void getData(){
        IMarket marketObj = RetrofitHelper.create(IMarket.class);
        marketObj.getMarketList()
                .enqueue(new MsgCallBack<MarketListBean>(getContext()) {
                    @Override
                    public void onErrored(Call<MarketListBean> call, Throwable t) {
                        mRefresh.finishRefresh();
                    }

                    @Override
                    public void onSuccessed(Call<MarketListBean> call, Response<MarketListBean> response) {
                        mRefresh.finishRefresh();
                        MarketListBean.Data[] data = response.body().getData();
                        List<MarketListBean.Data> list = new ArrayList<>();
                        for (int i = 0;i < data.length;i++){
                            list.add(data[i]);
                        }
                        mAdapter = new MarketAdapter(list,getContext());
                        mAdapter.setOnSectionClick(MarketFragment.this);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                });
    }

    @Override
    public void onSectionClick(String mid, int position) {
        Intent intent = new Intent(getContext(),MarketDetailModeActivity.class);
        intent.putExtra("mid",mid);
        startActivity(intent);
    }
}
