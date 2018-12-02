package com.example.wjh.zhilibaoproject.ui.fragement;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.wjh.zhilibaoproject.R;
import com.example.wjh.zhilibaoproject.adapter.ProductAdapter;
import com.example.wjh.zhilibaoproject.bean.ProductListBean;
import com.example.wjh.zhilibaoproject.network.RetrofitHelper;
import com.example.wjh.zhilibaoproject.network.api.IProduct;
import com.example.wjh.zhilibaoproject.network.callback.MsgCallBack;
import com.example.wjh.zhilibaoproject.ui.activity.ProductDetailActivity;
import com.example.wjh.zhilibaoproject.ui.fragement.base.BaseFragment;
import com.scwang.smartrefresh.header.TaurusHeader;
import com.scwang.smartrefresh.header.WaterDropHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class IndexFragment extends BaseFragment{
    private final static String TAG = IndexFragment.class.getSimpleName();
    private SmartRefreshLayout mRefresh;
    private RecyclerView mProductRv;
    private ProductAdapter adapter;
    private List<ProductListBean.Data> mList = null;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_index;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mProductRv = view.findViewById(R.id.product_rv);
//        mProductRv.setNestedScrollingEnabled(false);//禁止RecyclerView滑动
        mRefresh = view.findViewById(R.id.refresh);
        mRefresh.setEnableRefresh(true);
        mRefresh.setRefreshHeader(new TaurusHeader(getContext()));
        mRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                initData();
            }
        });

        adapter = new ProductAdapter(R.layout.item_product,null,getContext());
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getContext(),ProductDetailActivity.class);
                intent.putExtra("pid",mList.get(position).getId());
                startActivity(intent);
            }
        });
        mProductRv.setAdapter(adapter);
        mProductRv.setLayoutManager(new GridLayoutManager(getContext(),1, GridLayoutManager.VERTICAL,false));
    }

    @Override
    protected void initData() {
        super.initData();
        IProduct productObj  = RetrofitHelper.create(IProduct.class);
        productObj.getProductList()
                .enqueue(new MsgCallBack<ProductListBean>(getContext()) {
                    @Override
                    public void onErrored(Call<ProductListBean> call, Throwable t) {
                        mRefresh.finishRefresh();
                    }

                    @Override
                    public void onSuccessed(Call<ProductListBean> call, Response<ProductListBean> response) {
                        mRefresh.finishRefresh();
                        ProductListBean.Data[] data = response.body().getData();
                        Log.e(TAG,"data.length--------------------------->"+data.length);
                        List<ProductListBean.Data> list = new ArrayList<ProductListBean.Data>();
                        for (int i = 0;i < data.length;i++){
                            list.add(data[i]);
                        }
                        mList = list;//防止数组越界
                        if (mList.size() > 0){
                            setAdapter();
                        }
                    }
                });
    }

    private void setAdapter(){
        adapter.setNewData(mList);
    }
}
