package com.example.wjh.zhilibaoproject.ui.fragement;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.example.wjh.zhilibaoproject.R;
import com.example.wjh.zhilibaoproject.adapter.TitleRecyclerViewAdapter;
import com.example.wjh.zhilibaoproject.bean.ArticleTitleBean;
import com.example.wjh.zhilibaoproject.network.RetrofitHelper;
import com.example.wjh.zhilibaoproject.network.api.IArticle;
import com.example.wjh.zhilibaoproject.network.base.JsonItem;
import com.example.wjh.zhilibaoproject.network.callback.MsgCallBack;
import com.example.wjh.zhilibaoproject.ui.activity.ArticleDetailModeActivity;
import com.example.wjh.zhilibaoproject.ui.fragement.base.BaseFragment;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class EducationModeFragment extends BaseFragment implements TitleRecyclerViewAdapter.OnSectionClick {
    private final static String TAG = EducationModeFragment.class.getSimpleName();
    private String mCategory;
    private PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;
    private TitleRecyclerViewAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mode_education;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mPullLoadMoreRecyclerView =  view.findViewById(R.id.pullLoadMoreRecyclerView);
        mPullLoadMoreRecyclerView.setLinearLayout();
    }

    @Override
    protected void initData() {
        super.initData();
        mCategory = getArguments().getString("category",null);
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
        if (mCategory != null){

            Log.e(TAG,"category----------------------------->"+mCategory);
            IArticle articleObj = RetrofitHelper.create(IArticle.class);
            articleObj.getCategoryArticle(RetrofitHelper.getBody(new JsonItem("category",mCategory)))
                    .enqueue(new MsgCallBack<ArticleTitleBean>(getContext()) {
                        @Override
                        public void onErrored(Call<ArticleTitleBean> call, Throwable t) {

                        }

                        @Override
                        public void onSuccessed(Call<ArticleTitleBean> call, Response<ArticleTitleBean> response) {
                            int status = response.body().getStatus();
                            Log.e(TAG,"status-------------------->"+status);
                            List<ArticleTitleBean.Data> list = new ArrayList<ArticleTitleBean.Data>();
                            ArticleTitleBean.Data[] data = response.body().getData();
                            for (int i = 0;i <data.length;i++){
                                //将服务器上获取到的值赋值给list
                                Log.e(TAG,mCategory+"-------------------->"+data[i]);
                                list.add(data[i]);
                            }

                            mAdapter = new TitleRecyclerViewAdapter(list,getContext());
                            mAdapter.setOnSectionClick(EducationModeFragment.this);
                            mPullLoadMoreRecyclerView.setAdapter(mAdapter);
                            //每次执行完加载或者刷新 关闭
                            mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                        }
                    });
        }
    }

    @Override
    public void onSectionClick(String title, int position) {
        Intent intent = new Intent(getContext(), ArticleDetailModeActivity.class);
        intent.putExtra("title",title);
        startActivity(intent);
    }
}
