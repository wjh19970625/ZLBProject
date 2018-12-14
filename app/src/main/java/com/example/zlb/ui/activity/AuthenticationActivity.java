package com.example.zlb.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.zlb.R;
import com.example.zlb.api.IUser;
import com.example.zlb.bean.CallBackBaseBean;

import com.wjh.utillibrary.base.ActionBarActivity;
import com.wjh.utillibrary.network.RetrofitHelper;
import com.wjh.utillibrary.network.base.JsonItem;
import com.wjh.utillibrary.network.callback.MsgCallBack;
import retrofit2.Call;
import retrofit2.Response;

public class AuthenticationActivity extends ActionBarActivity {
    private EditText mTrueName;
    private EditText mIdCard;
    private TextView mSubmit;
    private String trueName;
    private String idCard;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_authentication;
    }

    @Override
    public void initView() {
        super.initView();
        setCenterTitle("实名认证");

        mTrueName = (EditText) findViewById(R.id.true_name);
        mIdCard = (EditText) findViewById(R.id.id_card);
        mSubmit = (TextView) findViewById(R.id.submit);
    }

    @Override
    public void init() {
        super.init();
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                trueName = mTrueName.getText().toString();
                idCard = mIdCard.getText().toString();
                if (trueName != null && !trueName.equals("") && idCard != null && !idCard.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(AuthenticationActivity.this,R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                    builder.setTitle("实名认证")
                            .setMessage("认证成功后将无法修改数据")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    sendData();
                                    setResult(RESULT_OK);
                                    finish();
                                }
                            });
                    builder.show();
                }else {
                    showToast("请完善数据");
                }

            }
        });
    }

    private void sendData(){
        IUser userObj = RetrofitHelper.create(IUser.class);
        userObj.checkIdentity(RetrofitHelper.getBody(new JsonItem("name",trueName),new JsonItem("cnId",idCard)))
                .enqueue(new MsgCallBack<CallBackBaseBean>(AuthenticationActivity.this,true) {
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
                        }else {
                            showToast(msg);
                        }
                    }
                });
    }
}
