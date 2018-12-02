package com.example.wjh.zhilibaoproject.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wjh.zhilibaoproject.R;
import com.example.wjh.zhilibaoproject.bean.MarketListBean;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.wjh.zhilibaoproject.common.Config.SERVICE_URL;

public class MarketAdapter extends RecyclerView.Adapter {
    private List<MarketListBean.Data> list;
    private Context context;
    private OnSectionClick onSectionClick;

    public MarketAdapter(List<MarketListBean.Data> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setOnSectionClick(OnSectionClick onSectionClick) {
        this.onSectionClick = onSectionClick;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_market, parent, false);
        marketViewHolder viewHolder = new marketViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        String title = list.get(position).getTitle();
        String views = list.get(position).getViews();
        String author = list.get(position).getAuthor();
        String url = SERVICE_URL + "/static/image" + list.get(position).getPicture();
        if (title != null && !title.equals("")) {
            ((marketViewHolder) holder).mTitle.setText(title);
        }

        ((marketViewHolder) holder).mViews.setText(views);
        ((marketViewHolder) holder).mAuthor.setText(author);
        ((marketViewHolder) holder).mItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mid = list.get(position).getId();
                onSectionClick.onSectionClick(mid, position);
            }
        });
        Picasso
                .with(context)
                .load(url)
                .into(((marketViewHolder) holder).mPicture);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class marketViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout mItem;
        public ImageView mPicture;
        public TextView mTitle;
        public TextView mViews;
        public TextView mAuthor;

        public marketViewHolder(View itemView) {
            super(itemView);
            mItem = itemView.findViewById(R.id.item);
            mPicture = itemView.findViewById(R.id.picture);
            mTitle = itemView.findViewById(R.id.title);
            mViews = itemView.findViewById(R.id.views);
            mAuthor = itemView.findViewById(R.id.author);
        }
    }

    public interface OnSectionClick {
        void onSectionClick(String mid, int position);
    }
}
