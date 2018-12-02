package com.example.wjh.zhilibaoproject.utils;

import com.example.wjh.zhilibaoproject.ui.MyApplication;

public class UserInfoHelper {
    private static UserInfoHelper userInfoHelper;
    private String nickname;
    private String token;
    private String phoneNumber;
    private String password;
    private String state;
    private String trueName;

    private UserInfoHelper(){
        nickname = SaveUtils.getStringValue(MyApplication.application(),"nickname");
        token = SaveUtils.getStringValue(MyApplication.application(),"token");
        phoneNumber = SaveUtils.getStringValue(MyApplication.application(),"phoneNum");
        password = SaveUtils.getStringValue(MyApplication.application(),"password");
        state = SaveUtils.getStringValue(MyApplication.application(),"state");
        trueName = SaveUtils.getStringValue(MyApplication.application(),"trueName");

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
        SaveUtils.saveToSharePreference(MyApplication.application(),"nickname",nickname);
        SaveUtils.saveToSharePreference(MyApplication.application(),"token",token);
        SaveUtils.saveToSharePreference(MyApplication.application(),"password",password);
        SaveUtils.saveToSharePreference(MyApplication.application(),"trueName",trueName);
        SaveUtils.saveToSharePreference(MyApplication.application(),"phoneNum",phoneNum);
    }

    public void saveState(String state){
        SaveUtils.saveToSharePreference(MyApplication.application(),"state",state);
    }

    public void remove(){
        SaveUtils.removeToSharePreference(MyApplication.application(),"nickname");
        SaveUtils.removeToSharePreference(MyApplication.application(),"token");
        SaveUtils.removeToSharePreference(MyApplication.application(),"password");
        SaveUtils.removeToSharePreference(MyApplication.application(),"trueName");
        SaveUtils.removeToSharePreference(MyApplication.application(),"phoneNum");
        SaveUtils.removeToSharePreference(MyApplication.application(),"state");
    }
}
