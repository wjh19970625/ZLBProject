package com.example.wjh.zhilibaoproject.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wjh.zhilibaoproject.R;
import com.example.wjh.zhilibaoproject.bean.GetOneOrdersBean;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.wjh.zhilibaoproject.common.Config.SERVICE_URL;

public class OrdersRecyclerViewAdapter extends RecyclerView.Adapter {
    private List<GetOneOrdersBean.Data> list;
    private Context context;
    private OnSectionClick onSectionClick;

    public OrdersRecyclerViewAdapter(List<GetOneOrdersBean.Data> list, Context context){
        this.list = list;
        this.context = context;
    }

    public void setOnSectionClick(OnSectionClick onSectionClick){
        this.onSectionClick = onSectionClick;
    }

    public void setData(List<GetOneOrdersBean.Data> list){
        this.list.addAll(list) ;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order,parent,false);
        orderViewHolder viewHolder = new orderViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        String orderName = list.get(position).getOrderName();
        int state = list.get(position).getState();
        String orderDescription = list.get(position).getOrderDescription();
        String orderCharge = list.get(position).getOrderCharge();
        String url = SERVICE_URL + list.get(position).getImage();

        switch (state){
            case 0:
                ((orderViewHolder)holder).mIsPaid.setText("交易完成");

                break;

            case 1:
                ((orderViewHolder)holder).mIsPaid.setText("未处理");

                break;

            case 2:
                ((orderViewHolder)holder).mIsPaid.setText("处理中");
                break;

            case 3:
                ((orderViewHolder)holder).mIsPaid.setText("待评价");
                ((orderViewHolder)holder).mBtn.setVisibility(View.VISIBLE);
                ((orderViewHolder)holder).mAssess.setVisibility(View.VISIBLE);
                break;

            case 4:
                ((orderViewHolder)holder).mIsPaid.setText("未支付");
                ((orderViewHolder)holder).mBtn.setVisibility(View.VISIBLE);
                ((orderViewHolder)holder).mPay.setVisibility(View.VISIBLE);
                break;
        }

        ((orderViewHolder)holder).mOrderName.setText(orderName);
        ((orderViewHolder)holder).mOrderCharge.setText(orderCharge);
        ((orderViewHolder)holder).mOrderDescription.setText(orderDescription);

        ((orderViewHolder)holder).mItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String orderId = list.get(position).getOrderId();
                onSectionClick.onSectionClick(orderId,position);
            }
        });

        Picasso.with(context).load(url).fit().into(((orderViewHolder)holder).mImage);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class orderViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout mItem;
        private TextView mOrderName;
        private TextView mIsPaid;
        private TextView mOrderDescription;
        private TextView mOrderCharge;
        private RelativeLayout mBtn;
        private TextView mPay;
        private TextView mAssess;
        private ImageView mImage;
        public orderViewHolder(View itemView) {
            super(itemView);
            mItem = itemView.findViewById(R.id.item);
            mOrderName = itemView.findViewById(R.id.orderName);
            mIsPaid = itemView.findViewById(R.id.isPaid);
            mOrderDescription = itemView.findViewById(R.id.orderDescription);
            mOrderCharge = itemView.findViewById(R.id.orderCharge);
            mBtn = itemView.findViewById(R.id.btn);
            mPay = itemView.findViewById(R.id.pay);
            mAssess = itemView.findViewById(R.id.assess);
            mImage = itemView.findViewById(R.id.image);
        }
    }

    public interface OnSectionClick{
        void onSectionClick(String orderId, int position);
    }
}
