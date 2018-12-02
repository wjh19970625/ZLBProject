package com.example.wjh.zhilibaoproject.ui.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wjh.zhilibaoproject.R;
import com.example.wjh.zhilibaoproject.bean.CreateOrdersBean;
import com.example.wjh.zhilibaoproject.bean.GetOneOrdersBean;
import com.example.wjh.zhilibaoproject.network.RetrofitHelper;
import com.example.wjh.zhilibaoproject.network.api.IOrder;
import com.example.wjh.zhilibaoproject.network.base.JsonItem;
import com.example.wjh.zhilibaoproject.network.callback.MsgCallBack;
import com.example.wjh.zhilibaoproject.ui.activity.base.ActionBarActivity;
import com.example.wjh.zhilibaoproject.utils.UserInfoHelper;
import com.squareup.picasso.Picasso;

import javax.security.auth.login.LoginException;

import retrofit2.Call;
import retrofit2.Response;

public class SubmitOrderActivity extends ActionBarActivity {
    private TextView mPhoneNumber;
    private TextView mTrueName;//真实姓名
    private TextView mName;//商品名
    private TextView mDescription;
    private TextView mServiceCharge;
    private TextView mOfficialCharge;
    private TextView mNum;
    private TextView mSum;
    private TextView mSum1;
    private TextView mCommit;
    private ImageView mPicture;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_submit_order;
    }

    @Override
    public void initView() {
        super.initView();
        setCenterTitle("订单确认");
        mPhoneNumber = (TextView) findViewById(R.id.phoneNumber);
        mName = (TextView) findViewById(R.id.name);
        mTrueName = (TextView) findViewById(R.id.trueName);
        mDescription = (TextView) findViewById(R.id.description);
        mServiceCharge = (TextView) findViewById(R.id.serviceCharge);
        mOfficialCharge = (TextView) findViewById(R.id.officialCharge);
        mNum = (TextView) findViewById(R.id.num);
        mSum = (TextView) findViewById(R.id.sum);
        mSum1 = (TextView) findViewById(R.id.sum1);
        mCommit = (TextView) findViewById(R.id.commit);
        mPicture = (ImageView) findViewById(R.id.picture);

    }

    @Override
    public void initData() {
        super.initData();
        Intent intent = getIntent();
        final String name = intent.getStringExtra("name");
        final String description = intent.getStringExtra("description");
        String serviceCharge = intent.getStringExtra("serviceCharge");
        String officialCharge = intent.getStringExtra("officialCharge");
        int num = intent.getIntExtra("num",0);
        final int sum = intent.getIntExtra("sum",0);
        String url = intent.getStringExtra("url");
        String phoneNum = UserInfoHelper.getInstance().getPhoneNumber();
        String trueName = UserInfoHelper.getInstance().getTrueName();

        mPhoneNumber.setText(phoneNum);
        mTrueName.setText(trueName);
        mName.setText(name);
        mDescription.setText(description);
        mServiceCharge.setText(serviceCharge);
        mOfficialCharge.setText(officialCharge);
        mNum.setText(num+"");
        mSum.setText(sum+"");
        mSum1.setText(sum+"");
        Picasso.with(SubmitOrderActivity.this).load(url).into(mPicture);

        mCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IOrder orderObj = RetrofitHelper.create(IOrder.class);
                orderObj.createOrder(RetrofitHelper.getBody(new JsonItem("orderName",name),new JsonItem("orderDescription",description),new JsonItem("orderCharge",sum)))
                        .enqueue(new MsgCallBack<CreateOrdersBean>(SubmitOrderActivity.this,true) {
                            @Override
                            public void onErrored(Call<CreateOrdersBean> call, Throwable t) {

                            }

                            @Override
                            public void onSuccessed(Call<CreateOrdersBean> call, Response<CreateOrdersBean> response) {
                                if (response.body().getData() == null){
                                    showToast("登录失效请重新登录");
                                    //清空数据
                                    UserInfoHelper.getInstance().remove();
                                }else {
                                    Log.e("OrderId","-------------------------->"+response.body().getData().getOrderId());
                                    int status = response.body().getStatus();
                                    if (status == 0){
                                        showToast("提交订单成功");
                                        //订单Id
                                        String orderId = response.body().getData().getOrderId();
                                        Intent intent = new Intent(SubmitOrderActivity.this,PayActivity.class);
                                        intent.putExtra("orderId",orderId);
                                        startActivity(intent);
                                        finish();
                                    }else {
                                        showToast("提交订单失败");
                                    }
                                }
                            }
                        });
            }
        });
    }
}
