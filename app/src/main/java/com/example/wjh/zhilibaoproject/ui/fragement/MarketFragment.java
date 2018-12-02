package com.example.wjh.zhilibaoproject.ui.fragement;

import android.content.Intent;
import android.view.View;

import com.example.wjh.zhilibaoproject.R;
import com.example.wjh.zhilibaoproject.adapter.MarketAdapter;
import com.example.wjh.zhilibaoproject.bean.MarketListBean;
import com.example.wjh.zhilibaoproject.network.RetrofitHelper;
import com.example.wjh.zhilibaoproject.network.api.IMarket;
import com.example.wjh.zhilibaoproject.network.callback.MsgCallBack;
import com.example.wjh.zhilibaoproject.ui.activity.MarketDetailModeActivity;
import com.example.wjh.zhilibaoproject.ui.fragement.base.BaseFragment;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class MarketFragment extends BaseFragment implements MarketAdapter.OnSectionClick {
    private PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;
    private MarketAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_market;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mPullLoadMoreRecyclerView = view.findViewById(R.id.pullLoadMoreRecyclerView);
        mPullLoadMoreRecyclerView.setStaggeredGridLayout(2);//设置网格布局
    }

    @Override
    protected void initData() {
        super.initData();
        getData();

    }

    @Override
    protected void init() {
        super.init();
        mPullLoadMoreRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                getData();
            }

            @Override
            public void onLoadMore() {
                //等后台更新接口
                mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
            }
        });
    }

    private void getData(){
        IMarket marketObj = RetrofitHelper.create(IMarket.class);
        marketObj.getMarketList()
                .enqueue(new MsgCallBack<MarketListBean>(getContext()) {
                    @Override
                    public void onErrored(Call<MarketListBean> call, Throwable t) {

                    }

                    @Override
                    public void onSuccessed(Call<MarketListBean> call, Response<MarketListBean> response) {
                        MarketListBean.Data[] data = response.body().getData();
                        List<MarketListBean.Data> list = new ArrayList<>();
                        for (int i = 0;i < data.length;i++){
                            list.add(data[i]);
                        }
                        mAdapter = new MarketAdapter(list,getContext());
                        mAdapter.setOnSectionClick(MarketFragment.this);
                        mPullLoadMoreRecyclerView.setAdapter(mAdapter);
                        //每次执行完加载或者刷新 关闭
                        mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
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
