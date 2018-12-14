package com.example.wjh.zlb.ui.activity;

import android.content.Intent;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;

import com.example.wjh.zlb.R;
import com.example.wjh.zlb.bean.ArticleDetailBean;
import com.example.wjh.zlb.network.RetrofitHelper;
import com.example.wjh.zlb.network.api.IArticle;
import com.example.wjh.zlb.network.base.JsonItem;
import com.example.wjh.zlb.network.callback.MsgCallBack;
import com.example.wjh.zlb.ui.activity.base.ActionBarActivity;
import com.example.wjh.zlb.utils.Utils;

import retrofit2.Call;
import retrofit2.Response;

public class ArticleDetailModeActivity extends ActionBarActivity {
    private final static String TAG = ArticleDetailModeActivity.class.getSimpleName();
    private TextView mTitle;
    private TextView mTime;
    private String title;
    private WebView mWebView;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_article_detail_mode;
    }

    @Override
    public void initView() {
        super.initView();
        setCenterTitle("文章详情");
        mTitle = (TextView) findViewById(R.id.title);
        mTime = (TextView) findViewById(R.id.update_time);
        mWebView = (WebView) findViewById(R.id.webView);
    }

    @Override
    public void initData() {
        super.initData();
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        if (title != null) {
            IArticle article = RetrofitHelper.create(IArticle.class);
            article.getArticleDetails(RetrofitHelper.getBody(new JsonItem("title",title)))
                    .enqueue(new MsgCallBack<ArticleDetailBean>(ArticleDetailModeActivity.this,true) {
                        @Override
                        public void onFailed(Call<ArticleDetailBean> call, Throwable t) {

                        }

                        @Override
                        public void onSucceed(Call<ArticleDetailBean> call, Response<ArticleDetailBean> response) {
                            int status = response.body().getStatus();
                            if (status == 0){
                                String content = response.body().getData().getContent();
                                long time = response.body().getData().getUpdate_time();
                                String timeStr = Utils.timeStamp2Date(time,null);

                                mTitle.setText(title);
                                mTime.setText(timeStr);
                                mWebView.loadData(content,"text/html; charset=utf-8", "utf-8");

                                Log.e(TAG,"content------------>"+content);
                            }else {
                                showToast("获取信息失败");
                                finish();
                            }
                        }
                    });
        }
    }

}