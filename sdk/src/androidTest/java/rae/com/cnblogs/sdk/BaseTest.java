package rae.com.cnblogs.sdk;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Before;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by ChenRui on 2016/11/30 00:16.
 */
@RunWith(AndroidJUnit4.class)
public class BaseTest {

    protected Context mContext;

    private final CountDownLatch mCountDownLatch = new CountDownLatch(1);

    @Before
    public void setup() {
        mContext = InstrumentationRegistry.getTargetContext();
    }

    public void run(Runnable runnable) throws InterruptedException {
        new Thread(runnable).run();
        mCountDownLatch.await(5, TimeUnit.MINUTES);
    }

    public void stop() {
        mCountDownLatch.countDown();
    }

    public void stop(Object msg, Object... args) {
        log(msg, args);
        stop();
    }

    public void log(Object msg, Object... args) {
        Log.d("Rae", String.format(msg.toString(), args));
    }

    public void error(Object msg, Object... args) {
        Log.e("Rae", String.format(msg.toString(), args));
    }
}