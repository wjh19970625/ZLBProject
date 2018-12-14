package com.example.wjh.zlb.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.wjh.zlb.R;
import com.example.wjh.zlb.bean.CallBackBaseBean;
import com.example.wjh.zlb.network.RetrofitHelper;
import com.example.wjh.zlb.network.api.IUser;
import com.example.wjh.zlb.network.base.JsonItem;
import com.example.wjh.zlb.network.callback.MsgCallBack;
import com.example.wjh.zlb.ui.activity.base.ActionBarActivity;
import com.example.wjh.zlb.utils.Utils;

import retrofit2.Call;
import retrofit2.Response;

public class RegisterActivity extends ActionBarActivity {
    private EditText mNickname;
    private EditText mPassword;
    private EditText mPasswordSubmit;
    private EditText mPhoneNumber;
    private EditText mVerificationCode;
    private TextView mGetPinTv;
    private TextView mRegisterBtn;
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
        setCenterTitle("注册");
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_register;
    }
    @Override
    public void initView() {
        super.initView();
        mNickname = (EditText) findViewById(R.id.nickname);
        mPassword = (EditText) findViewById(R.id.password);
        mPasswordSubmit = (EditText) findViewById(R.id.password_submit);
        mPhoneNumber = (EditText) findViewById(R.id.phoneNumber);
        mGetPinTv = (TextView) findViewById(R.id.get_pin_tv);
        mVerificationCode = (EditText) findViewById(R.id.verificationCode);
        mRegisterBtn = (TextView) findViewById(R.id.register_btn);
    }

    @Override
    public void init() {
        super.init();
        mGetPinTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = mPhoneNumber.getText().toString();
                if (phoneNumber != null && !phoneNumber.equals("")){
                    if(Utils.isPhoneNumber(phoneNumber)){
                        mGetPinTv.setEnabled(false);
                        mGetPinTv.setText(count+Str);
                        handler.sendEmptyMessage(CODE_WHAT);

                        IUser userObj = RetrofitHelper.create(IUser.class);
                        userObj.sendCheckCode(RetrofitHelper.getBody(new JsonItem("phoneNumber",phoneNumber)))
                                .enqueue(new MsgCallBack<CallBackBaseBean>(RegisterActivity.this,true) {
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
            }
        });

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String nickname = mNickname.getText().toString();
                final String password = mPassword.getText().toString();
                final String passwordSubmit = mPasswordSubmit.getText().toString();
                final String phoneNumber = mPhoneNumber.getText().toString();
                final String verificationCode = mVerificationCode.getText().toString();
                if (nickname !=null &&!nickname.equals("")){
                    if (password !=null &&!password.equals("")){
                        if (passwordSubmit !=null &&!passwordSubmit.equals("")){
                            if (passwordSubmit.equals(password)){
                                if (phoneNumber !=null &&!phoneNumber.equals("")){
                                    if (verificationCode !=null &&!verificationCode.equals("")){
                                        IUser user = RetrofitHelper.create(IUser.class);
                                        user.checkNickname(RetrofitHelper.getBody(new JsonItem("nickname",nickname)))
                                                .enqueue(new MsgCallBack<CallBackBaseBean>(RegisterActivity.this) {
                                                    @Override
                                                    public void onFailed(Call<CallBackBaseBean> call, Throwable t) {

                                                    }

                                                    @Override
                                                    public void onSucceed(Call<CallBackBaseBean> call, Response<CallBackBaseBean> response) {
                                                        int status = response.body().getStatus();
                                                        if (status == 0){
                                                            IUser userObj = RetrofitHelper.create(IUser.class);
                                                            userObj.register(RetrofitHelper.getBody(new JsonItem("nickname",nickname),new JsonItem("password",password)
                                                                    ,new JsonItem("phoneNumber",phoneNumber),new JsonItem("verificationCode",verificationCode)))
                                                                    .enqueue(new MsgCallBack<CallBackBaseBean>(RegisterActivity.this,true) {
                                                                        @Override
                                                                        public void onFailed(Call<CallBackBaseBean> call, Throwable t) {

                                                                        }

                                                                        @Override
                                                                        public void onSucceed(Call<CallBackBaseBean> call, Response<CallBackBaseBean> response) {
                                                                            int status = response.body().getStatus();
                                                                            if (status == 0){
                                                                                showToast("注册成功");
                                                                                setResult(RESULT_OK);
                                                                                finish();
                                                                            } else if (status == 1){
                                                                                showToast("验证码错误");
                                                                            }
                                                                        }
                                                                    });
                                                        } else if (status == 1){
                                                            showToast("“用户名已被使用");
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
