package com.example.zlb.api;

import com.example.zlb.bean.ArticleDetailBean;
import com.example.zlb.bean.ArticleTitleBean;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;


public interface IArticle {
    //获取文章标题列表
    @GET("/article/get_category_article.do")
    Call<ArticleTitleBean> getCategoryArticle(@QueryMap Map<String, String> map);

    //获取文章详情
    @GET("/article/get_article_details.do")
    Call<ArticleDetailBean> getArticleDetails(@QueryMap Map<String, String> map);

}
