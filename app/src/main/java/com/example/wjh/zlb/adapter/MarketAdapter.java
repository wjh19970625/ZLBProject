package com.example.wjh.zlb.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.wjh.zlb.R;
import com.example.wjh.zlb.bean.MarketListBean;
import com.example.wjh.zlb.ui.activity.MarketDetailModeActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.wjh.zlb.common.Config.SERVICE_URL;



public class MarketAdapter extends BaseQuickAdapter<MarketListBean.Data, BaseViewHolder> {
    private Context context;

    public MarketAdapter(Context context, @Nullable List<MarketListBean.Data> data) {
        super(R.layout.item_market, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, final MarketListBean.Data item) {
        LinearLayout mItem = helper.getView(R.id.item);
        ImageView mPicture = helper.getView(R.id.picture);
        TextView mTitle = helper.getView(R.id.title);
        TextView mViews = helper.getView(R.id.views);
        TextView mAuthor = helper.getView(R.id.author);

        String title = item.getTitle();
        String views = item.getViews();
        String author = item.getAuthor();
        String url = SERVICE_URL + "/static/image" + item.getPicture();
        if (title != null && !title.equals("")) {
            mTitle.setText(title);
        }

        mViews.setText(views);
        mAuthor.setText(author);
        mItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mid = item.getId();
                Intent intent = new Intent(context, MarketDetailModeActivity.class);
                intent.putExtra("mid",mid);
                context.startActivity(intent);
            }
        });
        Picasso
                .with(context)
                .load(url)
                .into(mPicture);
    }

}
