package com.example.wjh.zlb.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.wjh.zlb.R;
import com.example.wjh.zlb.bean.GetOneOrdersBean;
import com.example.wjh.zlb.ui.activity.PayActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.wjh.zlb.common.Config.SERVICE_URL;

public class OrdersRecyclerViewAdapter extends BaseQuickAdapter<GetOneOrdersBean.Data, BaseViewHolder> {
    private int mState;
    private Context context;

    public OrdersRecyclerViewAdapter(Context context, int state, @Nullable List<GetOneOrdersBean.Data> data) {
        super(R.layout.item_order, data);
        this.mState = state;
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, final GetOneOrdersBean.Data item) {
        LinearLayout mItem = helper.getView(R.id.item);
        TextView mOrderName = helper.getView(R.id.orderName);
        TextView mIsPaid = helper.getView(R.id.isPaid);
        TextView mOrderDescription = helper.getView(R.id.orderDescription);
        TextView mOrderCharge = helper.getView(R.id.orderCharge);
        RelativeLayout mBtn = helper.getView(R.id.btn);
        TextView mPay = helper.getView(R.id.pay);
        TextView mAssess = helper.getView(R.id.assess);
        ImageView mImage = helper.getView(R.id.image);

        String orderName = item.getOrderName();
        final int state = item.getState();
        String orderDescription = item.getOrderDescription();
        String orderCharge = item.getOrderCharge();
        String url = SERVICE_URL + item.getImage();

        switch (state){
            case 0:
                mIsPaid.setText("交易完成");
                break;
            case 1:
                mIsPaid.setText("未处理");
                break;
            case 2:
                mIsPaid.setText("处理中");
                break;
            case 3:
                mIsPaid.setText("待评价");
                mBtn.setVisibility(View.VISIBLE);
                mAssess.setVisibility(View.VISIBLE);
                break;
            case 4:
                mIsPaid.setText("未支付");
                mBtn.setVisibility(View.VISIBLE);
                mPay.setVisibility(View.VISIBLE);
                break;
        }

        mOrderName.setText(orderName);
        mOrderCharge.setText(orderCharge);
        mOrderDescription.setText(orderDescription);
        Picasso.with(mContext).load(url).fit().into(mImage);

        mItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String orderId = item.getOrderId();
                Intent intent = new Intent();
                intent.setClass(context, PayActivity.class);
                intent.putExtra("orderId",orderId);
                intent.putExtra("state",mState);
                context.startActivity(intent);
            }
        });
    }

}
