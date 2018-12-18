package com.example.zlb.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.example.zlb.R;
import com.example.zlb.api.IOrder;
import com.example.zlb.api.IProduct;
import com.example.zlb.bean.CallBackBaseBean;
import com.example.zlb.bean.GetOrderDetailBean;

import java.util.Map;

import com.wjh.utillibrary.base.ActionBarActivity;
import com.wjh.utillibrary.network.RetrofitHelper;
import com.wjh.utillibrary.network.base.JsonItem;
import com.wjh.utillibrary.network.callback.MsgCallBack;
import retrofit2.Call;
import retrofit2.Response;
import com.wjh.utillibrary.utils.PayResult;
import com.wjh.utillibrary.utils.UserInfoHelper;
import com.wjh.utillibrary.utils.Utils;

import static com.wjh.utillibrary.common.Config.SERVICE_URL;


public class PayActivity extends ActionBarActivity {
    private final static String TAG = PayActivity.class.getSimpleName();
    private TextView mPhoneNumber;
    private TextView mTrueName;//真实姓名
    private TextView mOrderName;
    private TextView mOrderDescription;
    private TextView mOrderCharge;
    private TextView mOrderId;
    private TextView mPayTime;
    private TextView mPay;
    private ImageView mImage;
    private LinearLayout mPayLl;
    private String orderId;
    private int mState;
    private PayHandler mHandler = new PayHandler();

    private static final int SDK_PAY_FLAG = 1;

    class PayHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case SDK_PAY_FLAG:
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        showToast("支付成功");
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        showToast("支付失败");
                    }
                    break;
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_pay;
    }

    @Override
    public void initView() {
        super.initView();
        setCenterTitle("确认支付");
        mPhoneNumber = (TextView) findViewById(R.id.phoneNumber);
        mTrueName = (TextView) findViewById(R.id.trueName);
        mOrderName = (TextView) findViewById(R.id.orderName);
        mOrderDescription = (TextView) findViewById(R.id.orderDescription);
        mOrderCharge = (TextView) findViewById(R.id.orderCharge);
        mOrderId = (TextView) findViewById(R.id.orderId);
        mPayTime = (TextView) findViewById(R.id.payTime);
        mPay = (TextView) findViewById(R.id.pay);
        mPayLl = (LinearLayout) findViewById(R.id.pay_ll);
        mImage = (ImageView) findViewById(R.id.image);
    }

    @Override
    public void initData() {
        super.initData();
        Intent intent = getIntent();
        orderId = intent.getStringExtra("orderId");
        mState = intent.getIntExtra("state",4);

        if (orderId == null || orderId.equals("")){
            return;
        }else {
            IOrder orderObj = RetrofitHelper.create(IOrder.class);
            orderObj.getOrderDetails(RetrofitHelper.getBody(new JsonItem("orderId",orderId)))
                    .enqueue(new MsgCallBack<GetOrderDetailBean>(PayActivity.this,true) {
                        @Override
                        public void onFailed(Call<GetOrderDetailBean> call, Throwable t) {

                        }

                        @Override
                        public void onSucceed(Call<GetOrderDetailBean> call, Response<GetOrderDetailBean> response) {
                            if (response.body() == null){
                                return;
                            }
                            String orderName = response.body().getData().getOrderName();
                            String orderDescription = response.body().getData().getOrderDescription();
                            String orderCharge = response.body().getData().getOrderCharge();
                            String payTime = Utils.timeStamp2Date(response.body().getData().getPayTime(),null);
                            String phoneNum = response.body().getData().getContact();
                            String trueName = response.body().getData().getName();
                            String url = SERVICE_URL + "/static/image" + response.body().getData().getImage();

                            mOrderName.setText(orderName);
                            mOrderDescription.setText(orderDescription);
                            mOrderCharge.setText("¥"+orderCharge);
                            mPayTime.setText(payTime);
                            mOrderId.setText(orderId);
                            mPhoneNumber.setText(phoneNum);
                            mTrueName.setText(trueName);
                            Glide.with(PayActivity.this).load(url).into(mImage);
                            Log.e(TAG,""+response.body().getData().getOrderCharge());
                        }
                    });
        }
    }

    @Override
    public void init() {
        super.init();
        //state 4 为未支付状态
        if (mState == 4){
            mPayLl.setVisibility(View.VISIBLE);
        }

        mPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (orderId == null || orderId.equals("")){
                    return;
                }else {
                    IProduct productObj = RetrofitHelper.create(IProduct.class);
                    productObj.pay(RetrofitHelper.getBody(new JsonItem("orderId",orderId)))
                            .enqueue(new MsgCallBack<CallBackBaseBean>(PayActivity.this,true) {
                                @Override
                                public void onFailed(Call<CallBackBaseBean> call, Throwable t) {

                                }

                                @Override
                                public void onSucceed(Call<CallBackBaseBean> call, Response<CallBackBaseBean> response) {
                                    int status = response.body().getStatus();
                                    String msg = response.body().getMsg();
                                    if (status == 0){
                                        //用户已经完成认证
                                        String state = UserInfoHelper.getInstance().getState();
                                        if (state.equals("0")){
                                            //发情支付请求
                                            final String orderId = msg;
                                            Log.e(TAG,"orderId--------------------------->"+orderId);

                                            Runnable payRunnable = new Runnable() {
                                                @Override
                                                public void run() {
                                                    PayTask alipay = new PayTask(PayActivity.this);
                                                    Map<String, String> result = alipay.payV2(orderId, true);
                                                    Log.i("msp", result.toString());

                                                    Message msg = new Message();
                                                    msg.what = SDK_PAY_FLAG;
                                                    msg.obj = result;
                                                    mHandler.sendMessage(msg);
                                                }
                                            };

                                            Thread payThread = new Thread(payRunnable);
                                            payThread.start();

                                        }else{
                                            showToast("用户还未认证/认证未通过");
                                        }

                                    }else if (status == 1){
                                        showToast(msg);
                                    }
                                }

                            });
                }
            }
        });
    }
}
