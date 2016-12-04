package com.rae.cnblogs;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.rae.core.utils.RaeDateUtil;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.rae.cnblogs", appContext.getPackageName());
    }

    @Test
    public void testDate() {
        Log.d("rae", "测试今天:" + getDate("2016-12-04 10:30:00"));
        Log.d("rae", "测试昨天:" + getDate("2016-12-03 12:30"));
        Log.d("rae", "测试前天:" + getDate("2016-12-02 12:30"));
        Log.d("rae", "测试其他时间:" + getDate("2016-11-02 12:30"));
    }


    private String getDate(String text) {

        String regx = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}";
        Matcher matcher = Pattern.compile(regx).matcher(text);
        if (!matcher.find()) {
            return text;
        }

        text = matcher.group();
        Date target = parseDate(text);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parseDate(text));

        calendar.get(Calendar.DAY_OF_YEAR);
        calendar.get(Calendar.DAY_OF_MONTH);
        calendar.get(Calendar.DATE);



        return text;
    }

    private Date parseDate(String text) {
        Date target;
        try {
            target = RaeDateUtil.parse(text, "yyyy-MM-dd HH:mm");
        } catch (Exception e) {
            Log.e("rae", "解析出错!", e);
            target = new Date();
        }
        return target;
    }

}
