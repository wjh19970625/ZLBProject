package com.example.wjh.zlb.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.wjh.zlb.R;
import com.example.wjh.zlb.bean.ProductListBean;
import com.squareup.picasso.Picasso;


import java.util.List;

import static com.example.wjh.zlb.common.Config.SERVICE_URL;

public class ProductAdapter extends BaseQuickAdapter<ProductListBean.Data,BaseViewHolder> {
    private Context mContext;

    public ProductAdapter(int layoutResId, @Nullable List<ProductListBean.Data> data,Context context) {
        super(layoutResId, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ProductListBean.Data item) {
        String url = SERVICE_URL + "/static/image" +item.getPicture();
        TextView mName = helper.getView(R.id.name);
        TextView mServiceCharge = helper.getView(R.id.serviceCharge);
        TextView mOfficialCharge = helper.getView(R.id.officialCharge);
        ImageView mDetailsPic = helper.getView(R.id.details_pic);

        //加载网络图片并进行填充
        Picasso.with(mContext).load(url).into(mDetailsPic);
        mName.setText(item.getName());
        mServiceCharge.setText(item.getServiceCharge());
        mOfficialCharge.setText(item.getOfficialCharge());
    }

}
