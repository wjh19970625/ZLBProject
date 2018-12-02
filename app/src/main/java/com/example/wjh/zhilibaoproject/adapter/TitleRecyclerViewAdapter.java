package com.example.wjh.zhilibaoproject.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wjh.zhilibaoproject.R;
import com.example.wjh.zhilibaoproject.bean.ArticleTitleBean;
import com.example.wjh.zhilibaoproject.utils.Utils;

import java.util.List;

public class TitleRecyclerViewAdapter extends RecyclerView.Adapter {
    private List<ArticleTitleBean.Data> list;
    private Context context;
    private OnSectionClick onSectionClick;

    public TitleRecyclerViewAdapter(List<ArticleTitleBean.Data> list, Context context){
        this.list = list;
        this.context = context;
    }

    public void setOnSectionClick(OnSectionClick onSectionClick){
        this.onSectionClick = onSectionClick;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_title,parent,false);
        titleViewHolder viewHolder = new titleViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        //将时间戳转换成字符串
        String time = Utils.timeStamp2Date(list.get(position).getUpdateTime(),null);

        ((titleViewHolder)holder).mTitle.setText(list.get(position).getTitle());
        ((titleViewHolder)holder).mUpdateTime.setText(time);
        ((titleViewHolder)holder).mTitleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = list.get(position).getTitle();
                onSectionClick.onSectionClick(title,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class titleViewHolder extends RecyclerView.ViewHolder{
        public TextView mTitle;
        public TextView mUpdateTime;
        public LinearLayout mTitleLayout;
        public titleViewHolder(View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.title);
            mUpdateTime = itemView.findViewById(R.id.update_time);
            mTitleLayout = itemView.findViewById(R.id.title_layout);
        }
    }

    public interface OnSectionClick{
        void onSectionClick(String title,int position);
    }
}
