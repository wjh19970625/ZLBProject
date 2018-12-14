package com.example.zlb.ui.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.EnvUtils;
import com.awen.photo.photopick.controller.PhotoPagerConfig;
import com.example.zlb.R;
import com.example.zlb.api.IProduct;
import com.example.zlb.bean.ProductDetailBean;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.wjh.utillibrary.base.WidgetActivity;
import com.wjh.utillibrary.common.Config;
import crossoverone.statuslib.StatusUtil;
import com.wjh.utillibrary.network.RetrofitHelper;
import com.wjh.utillibrary.network.base.JsonItem;
import com.wjh.utillibrary.network.callback.MsgCallBack;
import retrofit2.Call;
import retrofit2.Response;
import com.wjh.utillibrary.utils.UserInfoHelper;

public class ProductDetailActivity extends WidgetActivity {
    private final static String TAG = ProductDetailActivity.class.getSimpleName();
    private TextView mName;
    private TextView mDescription;
    private TextView mServiceCharge;
    private TextView mOfficialCharge;
    private TextView mCall;
    private TextView mPay;
    private ImageView mImage;
    private ImageView mDetail;
    private LinearLayout  mFinish;
    private int num = 1;//商品数量默认为1
    private int sum;
    private int service_charge;
    private int official_charge;
    private String pid;
    private boolean isOk = false;
    private DetailHandler mHandler = new DetailHandler();
    private String description;
    private String name;
    private String serviceCharge;
    private String officialCharge;
    private String mUrl;
    private String mUrlImage;

    private static final int DATA_OK = 1001;
//    private static final int SDK_PAY_FLAG = 1;

    class DetailHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DATA_OK:
                    isOk = true;
                    break;

//                case SDK_PAY_FLAG:
//                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
//                    /**
//                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
//                     */
//                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
//                    String resultStatus = payResult.getResultStatus();
//                    // 判断resultStatus 为9000则代表支付成功
//                    if (TextUtils.equals(resultStatus, "9000")) {
//                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
//                        Toast.makeText(ProductDetailActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
//                    } else {
//                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
//                        Toast.makeText(ProductDetailActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
//                    }
//                    break;
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_product_detail;
    }

    @Override
    public void initView() {
        super.initView();
        mName = (TextView) findViewById(R.id.name);
        mDescription = (TextView) findViewById(R.id.description);
        mServiceCharge = (TextView) findViewById(R.id.serviceCharge);
        mOfficialCharge = (TextView) findViewById(R.id.officialCharge);
        mCall = (TextView) findViewById(R.id.call);
        mPay = (TextView) findViewById(R.id.pay);
        mFinish = (LinearLayout) findViewById(R.id.finish);
        mImage = (ImageView) findViewById(R.id.image);
        mDetail = (ImageView) findViewById(R.id.picture);
    }

    @Override
    public void initData() {
        super.initData();
        Intent intent = getIntent();
        pid = intent.getStringExtra("pid");

        IProduct productObj = RetrofitHelper.create(IProduct.class);
        productObj.getProductInfo(RetrofitHelper.getBody(new JsonItem("pid", pid)))
                .enqueue(new MsgCallBack<ProductDetailBean>(ProductDetailActivity.this, true) {
                    @Override
                    public void onFailed(Call<ProductDetailBean> call, Throwable t) {

                    }

                    @Override
                    public void onSucceed(Call<ProductDetailBean> call, Response<ProductDetailBean> response) {
                        if (response.body().getData() == null ){
                            showToast("获取数据失败");
                            return;
                        }
                        name = response.body().getData().getName();
                        description = response.body().getData().getDescription();
                        serviceCharge = response.body().getData().getServiceCharge();
                        officialCharge = response.body().getData().getOfficialCharge();

                        String defaultUrl = "/static/image";
                        //详情
                        mUrl = Config.SERVICE_URL + defaultUrl + response.body().getData().getDetails();
                        //商品
                        mUrlImage = Config.SERVICE_URL + defaultUrl + response.body().getData().getPicture();
                        Picasso
                                .with(ProductDetailActivity.this)
                                .load(mUrlImage)
                                .into(mImage);

                        Picasso
                                .with(ProductDetailActivity.this)
                                .load(mUrl)
                                .into(mDetail);

                        Log.e(TAG,"url------------------>"+mUrl+"picture-------------->"+mUrlImage);

                        mName.setText(name);
                        mDescription.setText(description);
                        mServiceCharge.setText(serviceCharge);
                        mOfficialCharge.setText(officialCharge);

                        service_charge = Integer.parseInt(serviceCharge);
                        official_charge = Integer.parseInt(officialCharge);

                        sum = service_charge + official_charge;

                        mHandler.sendEmptyMessage(DATA_OK);
                    }
                });
    }

    @Override
    public void init() {
        super.init();
        mCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //检查权限
                checkPermission();
                AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetailActivity.this,R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                builder.setTitle("电话咨询")
                        .setMessage("点击确认后将拨打电话123456789")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.e(TAG,"---------------->拨打咨询电话");
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:123456789"));
                                if (ActivityCompat.checkSelfPermission(ProductDetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
                                startActivity(intent);
                            }
                        });
                builder.show();
            }
        });

        mPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOk){
                    String token = UserInfoHelper.getInstance().getToken();
                    String state = UserInfoHelper.getInstance().getState();
                    if (token == null || token.equals("")){
                        showToast("用户还未登录");
                        return;
                    } else if (!state.equals("0")){
                        showToast("用户还未认证/认证未通过");
                        return;
                    }
                    showDialog();
                }
                else {
                    showToast("正在加载数据");
                }
            }
        });
        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUrlImage != null && !mUrlImage.equals("")){
                    ArrayList<String> list = new ArrayList<>();
                    list.add(mUrlImage);
                    new PhotoPagerConfig.Builder(ProductDetailActivity.this)
                            .setBigImageUrls(list)
                            .setSavaImage(true)
//                        .setPosition(2)
//                        .setSaveImageLocalPath("这里是你想保存的图片地址")
                            .build();
                }else {
                    showToast("未获得图片信息");
                }

            }
        });

        mDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUrl != null && !mUrl.equals("")){
                    ArrayList<String> list = new ArrayList<>();
                    list.add(mUrl);
                    new PhotoPagerConfig.Builder(ProductDetailActivity.this)
                            .setBigImageUrls(list)
                            .setSavaImage(true)
//                        .setPosition(2)
//                        .setSaveImageLocalPath("这里是你想保存的图片地址")
                            .build();
                }else {
                    showToast("未获得图片信息");
                }

            }
        });

        mFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void checkPermission() {
        //检查权限（NEED_PERMISSION）是否被授权 PackageManager.PERMISSION_GRANTED表示同意授权
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            //用户已经拒绝过一次，再次弹出权限申请对话框需要给用户一个解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
                    .CALL_PHONE)) {
                Toast.makeText(this, "请开通相关权限，否则无法正常使用本功能！", Toast.LENGTH_SHORT).show();
            }
            //申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1001);

        } else {
            Log.e("MainActivity", "checkPermission: 已经授权！");
        }
    }

    private void showDialog(){
        final Dialog bottomDialog = new Dialog(ProductDetailActivity.this, R.style.bottomDialog);
        View contentView = LayoutInflater.from(ProductDetailActivity.this).inflate(R.layout.dialog_pay, null);
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = ProductDetailActivity.this.getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.bottomDialogAnimation);

        TextView dialog_name;
        TextView dialog_description;
        TextView dialog_serviceCharge;
        TextView dialog_officialCharge;
        Button dialog_add;
        Button dialog_minus;
        TextView dialog_submit;
        final TextView dialog_sum;
        final TextView dialog_num;

        dialog_name = contentView.findViewById(R.id.name);
        dialog_description = contentView.findViewById(R.id.description);
        dialog_serviceCharge = contentView.findViewById(R.id.serviceCharge);
        dialog_officialCharge = contentView.findViewById(R.id.officialCharge);
        dialog_add = contentView.findViewById(R.id.add);
        dialog_minus = contentView.findViewById(R.id.minus);
        dialog_sum = contentView.findViewById(R.id.sum);
        dialog_num = contentView.findViewById(R.id.num);
        dialog_submit = contentView.findViewById(R.id.submit);

        dialog_name.setText(name);
        dialog_sum.setText(Integer.toString(sum));
        dialog_num.setText(Integer.toString(num));
        dialog_description.setText(description);
        dialog_serviceCharge.setText(serviceCharge);
        dialog_officialCharge.setText(officialCharge);

        dialog_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num = num + 1;
                sum = service_charge * num + official_charge;

                dialog_num.setText(Integer.toString(num));
                dialog_sum.setText(Integer.toString(sum));
            }
        });

        dialog_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOk) {
                    if (num != 1) {
                        num = num - 1;
                        sum = service_charge * num + official_charge;

                        dialog_num.setText(Integer.toString(num));
                        dialog_sum.setText(Integer.toString(sum));
                    } else {
                        showToast("不能再少啦");
                    }
                }
            }
        });

        dialog_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                paySubmit();
                Intent intent = new Intent(ProductDetailActivity.this,SubmitOrderActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("description",description);
                intent.putExtra("serviceCharge",serviceCharge);
                intent.putExtra("officialCharge",officialCharge);
                intent.putExtra("num",num);
                intent.putExtra("sum",sum);
                intent.putExtra("url",mUrlImage);
                startActivity(intent);
            }
        });

        bottomDialog.show();
    }

//    private void paySubmit(){
//        AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetailActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
//        builder.setTitle("支付确认")
//                .setMessage("服务商品:"+name+"\n"+"数量:"+num+"\n"+"合计:"+sum)
//                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface com.wjh.utillibrary.view.dialog, int which) {
//
//                    }
//                })
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface com.wjh.utillibrary.view.dialog, int which) {
//                        String WIDbody = description;
//                        String WIDsubject = name;
//                        double WIDtotal_amount = sum;
//                        String amount = String.format("%.2f",WIDtotal_amount);
//
//                        Log.e(TAG,"WIDtotal_amount------------>"+amount+"   "+WIDsubject+"   "+WIDbody);
//
//                        IProduct productObj = RetrofitHelper.create(IProduct.class);
//                        productObj.pay(RetrofitHelper.getBody(new JsonItem("WIDbody",WIDbody),new JsonItem("WIDsubject",WIDsubject),new JsonItem("WIDtotal_amount",amount)))
//                                .enqueue(new MsgCallBack<CallBackBaseBean>(ProductDetailActivity.this,true) {
//                                    @Override
//                                    public void onErrored(Call<CallBackBaseBean> call, Throwable t) {
//
//                                    }
//
//                                    @Override
//                                    public void onSuccessed(Call<CallBackBaseBean> call, Response<CallBackBaseBean> response) {
//                                        int status = response.body().getStatus();
//                                        String msg = response.body().getMsg();
//                                        if (status == 0){
//                                            //用户已经完成认证
//                                            String state = UserInfoHelper.getInstance().getState();
//                                            if (state.equals("0")){
//                                                //发情支付请求
//                                                final String orderId = msg;
//                                                Log.e(TAG,"orderId--------------------------->"+orderId);
//
//                                                Runnable payRunnable = new Runnable() {
//                                                    @Override
//                                                    public void run() {
//                                                        PayTask alipay = new PayTask(ProductDetailActivity.this);
//                                                        Map<String, String> result = alipay.payV2(orderId, true);
//                                                        Log.i("msp", result.toString());
//
//                                                        Message msg = new Message();
//                                                        msg.what = SDK_PAY_FLAG;
//                                                        msg.obj = result;
//                                                        mHandler.sendMessage(msg);
//                                                    }
//                                                };
//
//                                                Thread payThread = new Thread(payRunnable);
//                                                payThread.start();
//
//                                            }else{
//                                                showToast("该用户还未认证/认证未通过");
//                                            }
//
//                                        }else if (status == 1){
//                                            showToast(msg);
//                                        }
//                                    }
//                                });
//
//                    }
//                });
//        builder.show();
//    }
    @Override
    protected void setStatusColor() {
        StatusUtil.setUseStatusBarColor(this, Color.TRANSPARENT);
    }

    @Override
    protected void setSystemInvadeBlack() {
        // 第二个参数是是否沉浸,第三个参数是状态栏字体是否为黑色。
        StatusUtil.setSystemStatus(this, true, false);
    }
}
