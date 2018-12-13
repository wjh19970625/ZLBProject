package com.example.wjh.zhilibaoproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.wjh.zhilibaoproject.R;
import com.example.wjh.zhilibaoproject.bean.ArticleTitleBean;
import com.example.wjh.zhilibaoproject.ui.activity.ArticleDetailModeActivity;
import com.example.wjh.zhilibaoproject.utils.Utils;

import java.util.List;

public class TitleRecyclerViewAdapter extends BaseQuickAdapter<ArticleTitleBean.Data, BaseViewHolder> {
    private Context context;

    public TitleRecyclerViewAdapter(Context context, @Nullable List<ArticleTitleBean.Data> data) {
        super(R.layout.item_title, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper,final ArticleTitleBean.Data item) {
        TextView mTitle = helper.getView(R.id.title);
        TextView mUpdateTime = helper.getView(R.id.update_time);
        LinearLayout mTitleLayout = helper.getView(R.id.title_layout);

        String time = Utils.timeStamp2Date(item.getUpdateTime(),null);

        mTitle.setText(item.getTitle());
        mUpdateTime.setText(time);
        mTitleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = item.getTitle();
                Intent intent = new Intent(context, ArticleDetailModeActivity.class);
                intent.putExtra("title",title);
                context.startActivity(intent);
            }
        });
    }

}
