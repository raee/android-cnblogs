package rae.com.cnblogs.sdk;

import com.github.raee.runit.RUnitTestLogUtils;
import com.rae.core.sdk.ApiUiArrayListener;
import com.rae.core.sdk.ApiUiListener;
import com.rae.core.sdk.exception.ApiException;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by ChenRui on 2017/2/7 0007 15:03.
 */
public abstract class ApiTestRunnable<T> implements Runnable, ApiUiArrayListener<T> {
    private CountDownLatch mCountDownLatch;

    public void setCountDownLatch(CountDownLatch countDownLatch) {
        mCountDownLatch = countDownLatch;
    }

    public void stop() {
        mCountDownLatch.countDown();
    }

    @Override
    public void onApiSuccess(List<T> data) {
        RUnitTestLogUtils.print("rae-cnblogs", data);
        stop();
    }

    @Override
    public void onApiFailed(ApiException ex, String msg) {
        throw ex;
    }


    protected ApiUiListener<T> listener() {
        return new ApiUiListener<T>() {
            @Override
            public void onApiFailed(ApiException ex, String msg) {
                ApiTestRunnable.this.onApiFailed(ex, msg);
            }

            @Override
            public void onApiSuccess(T data) {
                RUnitTestLogUtils.print("rae-cnblogs", data);
                stop();
            }
        };
    }
}
