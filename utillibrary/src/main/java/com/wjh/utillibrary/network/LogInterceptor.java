package com.wjh.utillibrary.network;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import com.wjh.utillibrary.utils.UserInfoHelper;

/**
 * Created by wangjiehang on 2018/5/11.
 */

public class LogInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Request newRequest;
            Request.Builder builder = request.newBuilder();
            String token = UserInfoHelper.getInstance().getToken();
            if (null !=token && !"".equals(token)){

                builder.header("token",token);
            }
            newRequest = builder.build();
            Response response = chain.proceed(newRequest);
            return response.newBuilder().build();
        }
}
