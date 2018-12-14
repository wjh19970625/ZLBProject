package com.example.wjh.zlb.network;

import com.example.wjh.zlb.common.Config;
import com.example.wjh.zlb.network.base.JsonItem;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2018/4/25.
 */

public class RetrofitHelper {
    private static RetrofitHelper retrofitHelper;
    private Retrofit mRetrofit;

    protected RetrofitHelper(){

    }

    public static RetrofitHelper getRetrofitHelper(){
        if (retrofitHelper == null){
            retrofitHelper = new RetrofitHelper();
        }
        return retrofitHelper;
    }

    public Retrofit getRetrofit(){
        if (mRetrofit == null){
            synchronized (RetrofitHelper.class){
                if (mRetrofit == null){

                    OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder()
                            .readTimeout(10, TimeUnit.SECONDS)
                            .connectTimeout(10, TimeUnit.SECONDS);

                    GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(
                            new GsonBuilder()
                                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
                                    .create()
                    );
                    //生成retrofit对象
                    Retrofit.Builder builder = new Retrofit.Builder().baseUrl(Config.SERVICE_URL).addConverterFactory(gsonConverterFactory);
                    retrofitMachining(okHttpClient);
                    mRetrofit = builder.client(okHttpClient.build()).build();
                }
            }
        }
        return mRetrofit;
    }

   protected void retrofitMachining(OkHttpClient.Builder clientBuilder){
        clientBuilder.addInterceptor(new LogInterceptor());
   }

    public static <T> T create(Class<T> cls){
        return getRetrofitHelper().getRetrofit().create(cls);
    }

    public static Map<String, String> getBody(JsonItem... jsonItems){
        StringBuffer urlParams = new StringBuffer();
        Map<String, String> jo =new HashMap<String, String>();
        if (jsonItems != null && jsonItems.length > 0){
            for (JsonItem jsonItem : jsonItems){
                try {
                    if (jsonItem==null){
                        continue;
                    }
                    jo.put(jsonItem.getKey(),jsonItem.getValue().toString());
                    urlParams.append("&"+jsonItem.getKey()+"="+jsonItem.getValue().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return jo;
    }

    public static MultipartBody.Part getBody(File file){
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("uploadFile",file.getName(),requestBody);
        return part;
    }

}
