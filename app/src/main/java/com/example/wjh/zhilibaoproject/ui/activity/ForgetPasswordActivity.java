package com.example.wjh.zhilibaoproject.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.wjh.zhilibaoproject.R;
import com.example.wjh.zhilibaoproject.bean.CallBackBaseBean;
import com.example.wjh.zhilibaoproject.network.RetrofitHelper;
import com.example.wjh.zhilibaoproject.network.api.IUser;
import com.example.wjh.zhilibaoproject.network.base.JsonItem;
import com.example.wjh.zhilibaoproject.network.callback.MsgCallBack;
import com.example.wjh.zhilibaoproject.ui.activity.base.ActionBarActivity;
import com.example.wjh.zhilibaoproject.utils.Utils;

import retrofit2.Call;
import retrofit2.Response;

public class ForgetPasswordActivity extends ActionBarActivity {
    private EditText mNickname;
    private EditText mPassword;
    private EditText mPasswordSubmit;
    private EditText mPhoneNumber;
    private EditText mCode;
    private TextView mGetPinTv;
    private TextView mFind;
    private static final int CODE_WHAT = 1001;
    private int count = 60;
    private static  String Str = "(S)后重试";
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case CODE_WHAT:
                    if (count <= 0){
                        mGetPinTv.setEnabled(true);
                        count = 60;
                        mGetPinTv.setText("获取验证码");
                    }else {
                        count--;
                        mGetPinTv.setText(count+Str);
                        handler.sendEmptyMessageDelayed(CODE_WHAT,1000);
                        break;
                    }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCenterTitle("找回密码");
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_forget_password;
    }
    @Override
    public void initView() {
        super.initView();
        mNickname = (EditText) findViewById(R.id.nickname);
        mPassword = (EditText) findViewById(R.id.password);
        mPasswordSubmit = (EditText) findViewById(R.id.password_submit);
        mPhoneNumber = (EditText) findViewById(R.id.phoneNumber);
        mGetPinTv = (TextView) findViewById(R.id.get_pin_tv);
        mCode = (EditText) findViewById(R.id.code);
        mFind = (TextView) findViewById(R.id.find);
    }

    @Override
    public void init() {
        super.init();
        mGetPinTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = mPhoneNumber.getText().toString();
                String nickname = mNickname.getText().toString();
                if (nickname != null && !nickname.equals("")){
                    if (phoneNumber != null && !phoneNumber.equals("")){
                        if(Utils.isPhoneNumber(phoneNumber)){
                            mGetPinTv.setEnabled(false);
                            mGetPinTv.setText(count+Str);
                            handler.sendEmptyMessage(CODE_WHAT);

                            IUser userObj = RetrofitHelper.create(IUser.class);
                            userObj.forgetPasswordSendSMS(RetrofitHelper.getBody(new JsonItem("nickname",nickname),new JsonItem("phoneNumber",phoneNumber)))
                                    .enqueue(new MsgCallBack<CallBackBaseBean>(ForgetPasswordActivity.this,true) {
                                        @Override
                                        public void onFailed(Call<CallBackBaseBean> call, Throwable t) {

                                        }

                                        @Override
                                        public void onSucceed(Call<CallBackBaseBean> call, Response<CallBackBaseBean> response) {
                                            String msg = response.body().getMsg();
                                            showToast(msg);
                                        }
                                    });
                        }else {
                            showToast("请填写正确格式的手机号码");
                        }
                    }else {
                        showToast("请填写手机号码");
                    }
                }else {
                    showToast("请填写用户名");
                }

            }
        });

        mFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String nickname = mNickname.getText().toString();
                final String password = mPassword.getText().toString();
                final String passwordSubmit = mPasswordSubmit.getText().toString();
                final String phoneNumber = mPhoneNumber.getText().toString();
                final String code = mCode.getText().toString();
                if (nickname !=null &&!nickname.equals("")){
                    if (password !=null &&!password.equals("")){
                        if (passwordSubmit !=null &&!passwordSubmit.equals("")){
                            if (passwordSubmit.equals(password)){
                                if (phoneNumber !=null &&!phoneNumber.equals("")){
                                    if (code !=null &&!code.equals("")){
                                        IUser userObj = RetrofitHelper.create(IUser.class);
                                        userObj.forgetPassword(RetrofitHelper.getBody(new JsonItem("nickname",nickname),new JsonItem("password",password)
                                                ,new JsonItem("phoneNumber",phoneNumber),new JsonItem("code",code)))
                                                .enqueue(new MsgCallBack<CallBackBaseBean>(ForgetPasswordActivity.this,true) {
                                                    @Override
                                                    public void onFailed(Call<CallBackBaseBean> call, Throwable t) {

                                                    }

                                                    @Override
                                                    public void onSucceed(Call<CallBackBaseBean> call, Response<CallBackBaseBean> response) {
                                                        int status = response.body().getStatus();
                                                        String msg = response.body().getMsg();
                                                        if (status == 0){
                                                            showToast(msg);
                                                            finish();
                                                        } else if (status == 1){
                                                            showToast(msg);
                                                        }
                                                    }
                                                });

                                    }else {
                                        showToast("验证码不能为空");
                                    }
                                }else {
                                    showToast("手机号不能为空");
                                }
                            }else {
                                showToast("两次密码输入不同");
                            }
                        }else {
                            showToast("确认密码不能为空");
                        }
                    }else {
                        showToast("密码不能为空");
                    }
                }else {
                    showToast("用户名不能为空");
                }
            }
        });
    }

}
