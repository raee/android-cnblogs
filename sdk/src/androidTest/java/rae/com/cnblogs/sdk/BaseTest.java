package rae.com.cnblogs.sdk;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.github.raee.runit.RUnitTestLogUtils;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.CnblogsApiProvider;
import com.rae.cnblogs.sdk.db.DbCnblogs;
import com.rae.core.sdk.ApiUiArrayListener;
import com.rae.core.sdk.ApiUiListener;
import com.rae.core.sdk.exception.ApiException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 接口测试基类
 * Created by ChenRui on 2016/11/30 00:16.
 */
@RunWith(AndroidJUnit4.class)
public class BaseTest {

    protected Context mContext;

    private final CountDownLatch mCountDownLatch = new CountDownLatch(1);

    private Queue<ApiTestRunnable> mTestRunnables = new LinkedList<>();

    @Before
    public void setup() {
        mContext = InstrumentationRegistry.getTargetContext();
        DbCnblogs.init(mContext);
    }

    protected CnblogsApiProvider getApiProvider() {
        return CnblogsApiFactory.getInstance(mContext);
    }

    public void startTest(Runnable runnable) {
        try {
            InstrumentationRegistry.getInstrumentation().runOnMainSync(runnable);
            mCountDownLatch.await(5, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void put(ApiTestRunnable runnable) {
        mTestRunnables.add(runnable);
    }

    public void runTestGroup() {
        for (ApiTestRunnable runnable : mTestRunnables) {
            CountDownLatch countDownLatch = new CountDownLatch(1);
            runnable.setCountDownLatch(countDownLatch);
            runnable.run();
            try {
                countDownLatch.await(30, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }


    }

    public void stop() {
        mCountDownLatch.countDown();
    }

    public void stop(Object msg, Object... args) {
        log(msg, args);
        stop();
    }

    public void log(Object msg, Object... args) {
        // 超出大小
        int maxLength = 3 * 1024;
        String response = msg.toString();
        if (response.length() > maxLength) {
            for (int i = 0; i < response.length(); i += maxLength) {
                int len = i + maxLength;
                if (len < response.length())
                    Log.i("rae", response.substring(i, len));
                else
                    Log.i("rae", response.substring(i, response.length()));
            }
        } else {
            Log.i("rae", String.format(msg.toString(), args));
        }
    }

    public void error(Object msg, Object... args) {
        stop();
        Assert.fail(String.format(msg.toString(), args));
    }

    protected <T> ApiUiListener<T> listener(Class<T> cls) {
        return new ApiUiListener<T>() {
            @Override
            public void onApiFailed(ApiException ex, String msg) {
                error("错误信息：%s", msg);
            }

            @Override
            public void onApiSuccess(T data) {
                RUnitTestLogUtils.print("rae", data);
                stop();
            }
        };
    }

    protected <T> ApiUiArrayListener<T> listListener(final Class<T> cls) {
        return new ApiUiArrayListener<T>() {
            @Override
            public void onApiFailed(ApiException ex, String msg) {
                error("错误信息：%s", msg);
            }

            @Override
            public void onApiSuccess(List<T> data) {
                if (cls == String.class) {
                    for (T t : data) {
                        Log.d("rae", t.toString());
                    }
                } else {
                    RUnitTestLogUtils.print("rae", data);
                }
                stop();
            }
        };
    }
}
