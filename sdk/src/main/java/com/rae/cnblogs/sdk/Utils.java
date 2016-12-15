package com.rae.cnblogs.sdk;

import android.text.TextUtils;
import android.util.Log;

import com.rae.core.utils.RaeDateUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具类
 * Created by ChenRui on 2016/12/13 23:20.
 */
public final class Utils {

    public static String getDate(String text) {

        String regx = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}";
        Matcher matcher = Pattern.compile(regx).matcher(text);
        if (!matcher.find()) {
            return text;
        }

        text = matcher.group();
        String time = text.split(" ")[1];

        // 时间间隔
        long span = (System.currentTimeMillis() - parseDate(text).getTime()) / 1000;

        // 今天过去的时间
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long today = (System.currentTimeMillis() - calendar.getTimeInMillis()) / 1000;
        if (span < 0) {
        } else if (span < 60) {
            text = "刚刚";
        } else if (span < 3600) {
            text = (span / 60) + "分钟前";
        } else if (span < today) {
            text = "今天 " + time;
        } else if (span < today + 86400) {
            text = "昨天 " + time;
        } else if (span < today + 2 * 86400) {
            text = "前天 " + time;
        }


        return text;
    }

    public static Date parseDate(String text) {
        Date target;
        try {
            target = RaeDateUtil.parse(text, "yyyy-MM-dd HH:mm");
        } catch (Exception e) {
            Log.e("rae", "解析出错!", e);
            target = new Date();
        }
        return target;
    }


    public static String getNumber(String text) {
        Matcher matcher = Pattern.compile("\\d+").matcher(text);
        if (matcher.find()) {
            return matcher.group();
        }
        return text;
    }

    public static String getCount(String text) {
        if (TextUtils.isEmpty(text)) return "0";
        return text.trim();
    }
}
