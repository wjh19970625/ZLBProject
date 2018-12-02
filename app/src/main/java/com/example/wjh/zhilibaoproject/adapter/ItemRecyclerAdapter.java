package com.example.wjh.zhilibaoproject.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.wjh.zhilibaoproject.R;
import com.example.wjh.zhilibaoproject.bean.MeItemBean;


import java.util.List;

/**
 * Created by Administrator on 2018/5/10.
 */

public class ItemRecyclerAdapter extends BaseQuickAdapter<MeItemBean, BaseViewHolder> {

    public ItemRecyclerAdapter(int layoutResId, @Nullable List<MeItemBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MeItemBean item) {
        ImageView view = helper.getView(R.id.item_index_img);
        TextView title = helper.getView(R.id.item_index_title);

        view.setImageResource(item.getId());
        title.setText(item.getTitle());
    }


}
