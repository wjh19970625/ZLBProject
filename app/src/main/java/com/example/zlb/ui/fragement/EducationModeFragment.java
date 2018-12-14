package com.example.zlb.ui.fragement;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.zlb.R;
import com.example.zlb.adapter.TitleRecyclerViewAdapter;
import com.example.zlb.api.IArticle;
import com.example.zlb.bean.ArticleTitleBean;
import base.BaseFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import network.RetrofitHelper;
import network.base.JsonItem;
import network.callback.MsgCallBack;
import retrofit2.Call;
import retrofit2.Response;

public class EducationModeFragment extends BaseFragment{
    private final static String TAG = EducationModeFragment.class.getSimpleName();
    private String mCategory;
    private RecyclerView mRecyclerView;
    private TitleRecyclerViewAdapter mAdapter;
    private SmartRefreshLayout mRefresh;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mode_education;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mRecyclerView =  view.findViewById(R.id.rv_education);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new TitleRecyclerViewAdapter(getContext(), null);
        mRecyclerView.setAdapter(mAdapter);
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
        mCategory = getArguments().getString("category",null);
        mRefresh.autoRefresh();
    }

    private void getData(){
        if (mCategory != null){
            Log.e(TAG,"category----------------------------->"+mCategory);
            IArticle articleObj = RetrofitHelper.create(IArticle.class);
            articleObj.getCategoryArticle(RetrofitHelper.getBody(new JsonItem("category",mCategory)))
                    .enqueue(new MsgCallBack<ArticleTitleBean>(getContext()) {
                        @Override
                        public void onFailed(Call<ArticleTitleBean> call, Throwable t) {
                            mRefresh.finishRefresh();
                        }

                        @Override
                        public void onSucceed(Call<ArticleTitleBean> call, Response<ArticleTitleBean> response) {
                            mRefresh.finishRefresh();
                            int status = response.body().getStatus();
                            Log.e(TAG,"status-------------------->"+status);
                            List<ArticleTitleBean.Data> list = new ArrayList<ArticleTitleBean.Data>();
                            ArticleTitleBean.Data[] data = response.body().getData();
                            for (int i = 0;i <data.length;i++){
                                //将服务器上获取到的值赋值给list
                                Log.e(TAG,mCategory+"-------------------->"+data[i]);
                                list.add(data[i]);
                            }

                            mAdapter.setNewData(list);
                        }
                    });
        }
    }

}
