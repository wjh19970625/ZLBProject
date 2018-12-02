package com.example.wjh.zhilibaoproject.network.api;

import com.example.wjh.zhilibaoproject.bean.CallBackBaseBean;
import com.example.wjh.zhilibaoproject.bean.LoginBean;
import com.example.wjh.zhilibaoproject.bean.UserInfBean;

import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;

import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;

public interface IUser {
    //注册
    @POST("/user/register.do")
    Call<CallBackBaseBean> register(@QueryMap Map<String, String> map);
    //登录
    @POST("/user/login.do")
    Call<LoginBean> login(@QueryMap Map<String, String> map);
    //获取手机验证码
    @POST("/user/register_send_check_code.do")
    Call<CallBackBaseBean> sendCheckCode(@QueryMap Map<String, String> map);
    //检验用户名
    @POST("/user/check_nickname.do")
    Call<CallBackBaseBean> checkNickname(@QueryMap Map<String, String> map);
    //获取用户信息
    @POST("/user/get_user_info.do")
    Call<UserInfBean> getUserInfo();

    //更新用户信息
    @POST("/user/update_user_info.do")
    Call<CallBackBaseBean> updateUserInfo(@QueryMap Map<String, String> map);

    //修改密码
    @POST("/user/change_password.do")
    Call<CallBackBaseBean>changePassword(@QueryMap Map<String, String> map);

    //忘记密码
    @POST("/user/forget_password.do")
    Call<CallBackBaseBean>forgetPassword(@QueryMap Map<String, String> map);

    //忘记密码发送验证码
    @POST("/user/forget_password_send_sms.do")
    Call<CallBackBaseBean>forgetPasswordSendSMS(@QueryMap Map<String, String> map);

    //身份验证
    @POST("/user/check_identity.do?")
    Call<CallBackBaseBean>checkIdentity(@QueryMap Map<String, String> map);

    //上传接口
    @Multipart
    @POST("/user/head_image_upload.do")
    Call<CallBackBaseBean>upload(@Part() MultipartBody.Part file);
}
