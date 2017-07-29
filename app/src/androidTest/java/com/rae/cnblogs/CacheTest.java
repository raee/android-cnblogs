package com.rae.cnblogs;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * app data cache test
 * Created by ChenRui on 2017/7/28 0028 22:12.
 */
@RunWith(AndroidJUnit4.class)
public class CacheTest {

    private Context mContext;

    @Before
    public void setup() {
        mContext = InstrumentationRegistry.getContext();

    }

    @Test
    public void testCacheSize() {
//        new AppDataManager(mContext).getDatabaseTotalSize();
    }
}
