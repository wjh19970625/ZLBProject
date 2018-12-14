package com.example.zlb.ui.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.zlb.R;
import com.example.zlb.api.IIndex;
import com.example.zlb.bean.CallBackBaseBean;

import base.ActionBarActivity;
import network.RetrofitHelper;
import network.base.JsonItem;
import network.callback.MsgCallBack;
import retrofit2.Call;
import retrofit2.Response;

public class FeedBackActivity extends ActionBarActivity {
    private EditText mContent;
    private TextView mSubmit;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_feedback;
    }

    @Override
    public void initView() {
        super.initView();
        setCenterTitle("意见反馈");
        mContent = (EditText) findViewById(R.id.content);
        mSubmit = (TextView) findViewById(R.id.submit);
    }

    @Override
    public void init() {
        super.init();
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String suggest = mContent.getText().toString();
                if (suggest == null || suggest.equals("")){
                    showToast("输入内容不能为空");
                }else {
                    IIndex indexObj = RetrofitHelper.create(IIndex.class);
                    indexObj.suggest(RetrofitHelper.getBody(new JsonItem("suggest",suggest)))
                            .enqueue(new MsgCallBack<CallBackBaseBean>(FeedBackActivity.this,true) {
                                @Override
                                public void onFailed(Call<CallBackBaseBean> call, Throwable t) {

                                }

                                @Override
                                public void onSucceed(Call<CallBackBaseBean> call, Response<CallBackBaseBean> response) {
                                    int status = response.body().getStatus();
                                    if (status == 0){
                                        showToast("反馈成功");
                                    }else {
                                        showToast("反馈失败");
                                    }
                                }
                            });
                }
            }
        });
    }

}
