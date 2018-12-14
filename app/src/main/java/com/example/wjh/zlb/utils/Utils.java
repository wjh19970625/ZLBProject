package com.example.wjh.zlb.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    // 判断手机号码是否规则
    public static boolean isPhoneNumber(String input) {
        String regex = "(1[0-9][0-9]|15[0-9]|18[0-9])\\d{8}";
        Pattern p = Pattern.compile(regex);
        return p.matches(regex, input);
    }

    //解决自动换行问题
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i< c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }if (c[i]> 65280&& c[i]< 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }
    // 判断邮箱是否规则
    public static boolean isEmail(String email){
        if (null==email || "".equals(email)) return false;
        //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        Pattern p =  Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }
    //时间戳转换成字符串
    public static String timeStamp2Date(long time, String format) {
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(time));
    }

}
