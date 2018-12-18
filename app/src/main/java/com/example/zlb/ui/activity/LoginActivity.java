package com.example.zlb.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zlb.R;
import com.example.zlb.api.IUser;
import com.example.zlb.bean.LoginBean;

import com.wjh.utillibrary.base.WidgetActivity;
import com.wjh.utillibrary.network.RetrofitHelper;
import com.wjh.utillibrary.network.base.JsonItem;
import com.wjh.utillibrary.network.callback.MsgCallBack;
import crossoverone.statuslib.StatusUtil;
import retrofit2.Call;
import retrofit2.Response;

public class LoginActivity extends WidgetActivity {
    private EditText mNickname;
    private EditText mPassword;
    private TextView mForgetPassword;
    private Button mLoginBtn;
    private Button mRegisterBtn;
    private ImageView mIvBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }


    @Override
    public void initView() {
        super.initView();
        mNickname = (EditText) findViewById(R.id.nickname);
        mPassword = (EditText) findViewById(R.id.password);
        mForgetPassword = (TextView) findViewById(R.id.forget_password);
        mLoginBtn = (Button) findViewById(R.id.login_btn);
        mRegisterBtn = (Button) findViewById(R.id.register_btn);
        mIvBack = (ImageView) findViewById(R.id.back);
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void init() {
        super.init();
        mForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(ForgetPasswordActivity.class);
            }
        });

        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String nickname = mNickname.getText().toString();
                final String password = mPassword.getText().toString();
                if (nickname != null || !nickname.equals("") || password != null || !password.equals("")){
                    IUser userObj = RetrofitHelper.create(IUser.class);
                    userObj.login(RetrofitHelper.getBody(new JsonItem("nickname",nickname),new JsonItem("password",password)))
                            .enqueue(new MsgCallBack<LoginBean>(LoginActivity.this,true) {
                                @Override
                                public void onFailed(Call<LoginBean> call, Throwable t) {

                                }

                                @Override
                                public void onSucceed(Call<LoginBean> call, Response<LoginBean> response) {
                                    int status = response.body().getStatus();
                                    if (status == 0){
                                        showToast("登录成功");
                                        //保存需要的信息
                                        String token = response.body().getMsg();
                                        String phoneNum =response.body().getData().getPhoneNumber();
                                        String trueName = response.body().getData().getName();
                                        Intent intent = new Intent();
                                        intent.putExtra("nickname",nickname);
                                        intent.putExtra("token",token);
                                        intent.putExtra("password",password);
                                        intent.putExtra("phoneNum",phoneNum);
                                        intent.putExtra("trueName",trueName);
                                        setResult(RESULT_OK,intent);
                                        finish();
                                    } else if (status == 1){
                                        showToast("用户名或密码错误");
                                    }
                                }
                            });

                }


            }
        });

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(RegisterActivity.class);
            }
        });
    }

    @Override
    protected void setSystemInvadeBlack() {
        // 第二个参数是是否沉浸,第三个参数是状态栏字体是否为黑色。
        StatusUtil.setSystemStatus(this, true, false);
    }

}
