package com.wjh.utillibrary.utils;

import android.content.Context;

public class UserInfoHelper {
    private static UserInfoHelper userInfoHelper;
    private String nickname;
    private String token;
    private String phoneNumber;
    private String password;
    private String state;
    private String trueName;
    private static Context mContext;


    private UserInfoHelper(){
        nickname = SaveUtils.getStringValue(mContext,"nickname");
        token = SaveUtils.getStringValue(mContext,"token");
        phoneNumber = SaveUtils.getStringValue(mContext,"phoneNum");
        password = SaveUtils.getStringValue(mContext,"password");
        state = SaveUtils.getStringValue(mContext,"state");
        trueName = SaveUtils.getStringValue(mContext,"trueName");

    }

    public static void init(Context context){
        mContext = context.getApplicationContext();
    }

    public static UserInfoHelper getInstance(){
        if (userInfoHelper == null){
            userInfoHelper = new UserInfoHelper();
        }
        return userInfoHelper;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void save(String nickname,String password,String token,String trueName,String phoneNum){
        SaveUtils.saveToSharePreference(mContext,"nickname",nickname);
        SaveUtils.saveToSharePreference(mContext,"token",token);
        SaveUtils.saveToSharePreference(mContext,"password",password);
        SaveUtils.saveToSharePreference(mContext,"trueName",trueName);
        SaveUtils.saveToSharePreference(mContext,"phoneNum",phoneNum);
    }

    public void saveState(String state){
        SaveUtils.saveToSharePreference(mContext,"state",state);
    }

    public void remove(){
        SaveUtils.removeToSharePreference(mContext,"nickname");
        SaveUtils.removeToSharePreference(mContext,"token");
        SaveUtils.removeToSharePreference(mContext,"password");
        SaveUtils.removeToSharePreference(mContext,"trueName");
        SaveUtils.removeToSharePreference(mContext,"phoneNum");
        SaveUtils.removeToSharePreference(mContext,"state");
    }
}
