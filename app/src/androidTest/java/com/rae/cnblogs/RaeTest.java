package com.rae.cnblogs;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.rae.cnblogs.sdk.bean.BlogBean;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChenRui on 2017/2/25 0025 0:28.
 */
@RunWith(AndroidJUnit4.class)
public class RaeTest {

    @Test
    public void testString() {

        long memory = Runtime.getRuntime().freeMemory();
        String id;
        int size = 10;
        List<BlogBean> data = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            id = "id-" + i;
            BlogBean m = new BlogBean();
            m.setBlogId(id);
            data.add(m);
        }

        String a = "abc";
        String b = "abc";

        "".equals("");

        Log.w("rae", "是否登录=" + (a == b));

        for (BlogBean blogBean : data) {
            Log.i("test-rae", blogBean.getBlogId());
        }

    }


}
