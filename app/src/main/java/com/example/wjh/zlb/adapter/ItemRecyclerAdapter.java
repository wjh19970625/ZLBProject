package com.example.wjh.zlb.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.wjh.zlb.R;
import com.example.wjh.zlb.bean.MeItemBean;


import java.util.List;

/**
 * Created by wjh on 2018/12/13 0013   20:26
 * Description
 * Email: 2281837849@qq.com
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
