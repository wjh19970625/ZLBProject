package com.example.wjh.zhilibaoproject.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.awen.photo.photopick.controller.PhotoPagerConfig;
import com.example.wjh.zhilibaoproject.R;
import com.example.wjh.zhilibaoproject.bean.MarketDetailBean;
import com.example.wjh.zhilibaoproject.network.RetrofitHelper;
import com.example.wjh.zhilibaoproject.network.api.IMarket;
import com.example.wjh.zhilibaoproject.network.base.JsonItem;
import com.example.wjh.zhilibaoproject.network.callback.MsgCallBack;
import com.example.wjh.zhilibaoproject.ui.activity.base.WidgetActivity;
import com.example.wjh.zhilibaoproject.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

import static com.example.wjh.zhilibaoproject.common.Config.SERVICE_URL;

public class MarketDetailModeActivity extends WidgetActivity {
    private ImageView mPicture;
    private TextView mTitle;
    private TextView mAuthor;
    private TextView mViews;
    private TextView mContent;
    private TextView mCreateTime;
    private LinearLayout mFinish;
    private String mid;
    private String mUrl;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_market_detail_mode;
    }

    @Override
    public void initView() {
        super.initView();
        mPicture = (ImageView) findViewById(R.id.picture);
        mTitle = (TextView) findViewById(R.id.title);
        mAuthor = (TextView) findViewById(R.id.author);
        mViews = (TextView) findViewById(R.id.views);
        mContent = (TextView) findViewById(R.id.content);
        mFinish = (LinearLayout) findViewById(R.id.finish);
        mCreateTime = (TextView) findViewById(R.id.createTime);
    }

    @Override
    public void initData() {
        super.initData();
        Intent intent = getIntent();
        mid = intent.getStringExtra("mid");

        if (mid != null){
            IMarket marketOnj = RetrofitHelper.create(IMarket.class);
            marketOnj.getIdeaDetails(RetrofitHelper.getBody(new JsonItem("mid",mid)))
                    .enqueue(new MsgCallBack<MarketDetailBean>(MarketDetailModeActivity.this,true) {
                        @Override
                        public void onErrored(Call<MarketDetailBean> call, Throwable t) {

                        }

                        @Override
                        public void onSuccessed(Call<MarketDetailBean> call, Response<MarketDetailBean> response) {
                            MarketDetailBean.Data data = response.body().getData();

                            if (data != null){
                                mUrl = SERVICE_URL + "/static/image" + data.getPicture();
                                String title = data.getTitle();
                                String author = data.getAuthor();
                                String views = data.getViews();
                                String content = data.getContent();
                                long createTime = data.getCreateTime();
                                String time = Utils.timeStamp2Date(createTime,null);

                                Picasso
                                        .with(MarketDetailModeActivity.this)
                                        .load(mUrl)
                                        .into(mPicture);

                                mTitle.setText(title);
                                mAuthor.setText(author);
                                mViews.setText(views);
                                mContent.setText("      "+content);
                                mCreateTime.setText(time);
                            }
                        }
                    });
        }else {
            showToast("无法获取详情");
            return;
        }

    }

    @Override
    public void init() {
        super.init();
        mPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUrl != null){
                    ArrayList<String> list = new ArrayList<>();
                    list.add(mUrl);
                    new PhotoPagerConfig.Builder(MarketDetailModeActivity.this)
                            .setBigImageUrls(list)
                            .setSavaImage(true)
                            .build();
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
}
