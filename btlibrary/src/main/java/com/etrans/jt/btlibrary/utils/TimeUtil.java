package com.etrans.jt.btlibrary.utils;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by liyanze on 2016/9/1.
 */
public class TimeUtil {
    public static String getTopTime() {
        String result = "";
        SimpleDateFormat time_format = new SimpleDateFormat("HH:mm:aa", Locale.ENGLISH);//设置日期格式
        String time = time_format.format(new Date());
        if (!TextUtils.isEmpty(time)) {
            String[] split = time.split(":");
            result = split[0] + ":" + split[1]/* + " " + (split[2]).toLowerCase()*/;
        }
        return result;
    }
}
