package com.example.zlb.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.zlb.R;
import com.example.zlb.bean.ArticleTitleBean;
import com.example.zlb.ui.activity.ArticleDetailModeActivity;

import java.util.List;

import com.wjh.utillibrary.utils.Utils;

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
