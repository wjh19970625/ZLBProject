package com.example.wjh.zhilibaoproject.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by wjh on 2018/7/2.
 */

public class SaveUtils {
    private static final String SHARED_PREFERENCES_NAME = "ZhiLiBao";

    public static void saveToSharePreference(Context context,String key,String value){
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public static void saveToSharePreference(Context context,String key,int value){
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key,value);
        editor.commit();
    }

    public static void saveToSharePreference(Context context,String key,Long value){
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(key,value);
        editor.commit();
    }

    public static void saveToSharePreference(Context context,String key,boolean value){
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key,value);
        editor.commit();
    }

    public static void removeToSharePreference(Context context,String key) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }

    public static String getStringValue(Context context,String key){
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE);
        String value = sp.getString(key,"");
        return value;
    }

    public static int getIntValue(Context context,String key){
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE);
        int value = sp.getInt(key,0);
        return value;
    }

    public static Boolean getBooleanValue(Context context,String key){
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE);
        Boolean value = sp.getBoolean(key,false);
        return value;
    }

    public static Long getLongValue(Context context,String key){
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE);
        Long value = sp.getLong(key,0);
        return value;
    }

}
