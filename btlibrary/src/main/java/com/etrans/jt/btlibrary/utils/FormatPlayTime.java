package com.etrans.jt.btlibrary.utils;

import android.content.Context;

import com.etrans.jt.btlibrary.R;

import java.util.Formatter;
import java.util.Locale;

/**
 * 单元名称:FormatPlayTime.java
 * Created by fuxiaolei on 2016/7/14.
 * 说明:
 * Last Change by fuxiaolei on 2016/7/14.
 */
public class FormatPlayTime {
    private static StringBuilder sFormatBuilder = new StringBuilder();
    private static Formatter sFormatter = new Formatter(sFormatBuilder, Locale.getDefault());
    private static final Object[] sTimeArgs = new Object[5];

    public static String formatPlayTime(Context context, long secs) {
        String durationformat = context.getString(secs < 3600 ? R.string.durationformatshort :
                R.string.durationformatlong);
        sFormatBuilder.setLength(0);

        final Object[] timeArgs = sTimeArgs;
        timeArgs[0] = secs / 3600;
        timeArgs[1] = secs / 60;
        timeArgs[2] = (secs / 60) % 60;
        timeArgs[3] = secs;
        timeArgs[4] = secs % 60;

        return sFormatter.format(durationformat, timeArgs).toString();
    }
}
