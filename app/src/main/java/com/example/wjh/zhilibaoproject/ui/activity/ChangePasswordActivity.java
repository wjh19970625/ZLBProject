package com.example.wjh.zhilibaoproject.ui.activity;

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

import retrofit2.Call;
import retrofit2.Response;

public class ChangePasswordActivity extends ActionBarActivity {
    private EditText mOldPassword;
    private EditText mNewPassword;
    private EditText mSubmitPassword;
    private TextView mSubmit;

    @Override
    public void initView() {
        super.initView();
        setCenterTitle("修改密码");
        mOldPassword = (EditText) findViewById(R.id.old_password);
        mNewPassword = (EditText) findViewById(R.id.new_password);
        mSubmitPassword = (EditText) findViewById(R.id.new_password_submit);
        mSubmit = (TextView) findViewById(R.id.submit);
    }

    @Override
    public void init() {
        super.init();
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPassword = mOldPassword.getText().toString();
                String newPassword = mNewPassword.getText().toString();
                String submitNewPassword = mSubmitPassword.getText().toString();

                if (oldPassword == null || oldPassword.equals("") || newPassword == null || newPassword.equals("") || submitNewPassword == null || submitNewPassword.equals("")) {
                    showToast("请完善资料");
                }else {
                    if (!newPassword.equals(submitNewPassword)){
                        showToast("两次密码输入不同");
                    }else {
                        IUser userObj = RetrofitHelper.create(IUser.class);
                        userObj.changePassword(RetrofitHelper.getBody(new JsonItem("oldPassword",oldPassword),new JsonItem("newPassword",newPassword)))
                                .enqueue(new MsgCallBack<CallBackBaseBean>(ChangePasswordActivity.this,true) {
                                    @Override
                                    public void onErrored(Call<CallBackBaseBean> call, Throwable t) {

                                    }

                                    @Override
                                    public void onSuccessed(Call<CallBackBaseBean> call, Response<CallBackBaseBean> response) {
                                        int status = response.body().getStatus();
                                        String msg = response.body().getMsg();
                                        showToast(msg);
                                        if(status == 0){
                                            setResult(RESULT_OK);
                                            finish();
                                        }
                                    }
                                });
                    }
                }
            }
        });
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_change_password;
    }

}
