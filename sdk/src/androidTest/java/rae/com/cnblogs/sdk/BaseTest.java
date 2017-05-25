package rae.com.cnblogs.sdk;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import com.github.raee.runit.AndroidRUnit4ClassRunner;
import com.github.raee.runit.RUnitTestLogUtils;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.CnblogsApiProvider;

import org.junit.Before;
import org.junit.runner.RunWith;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * 接口测试基类
 * Created by ChenRui on 2016/11/30 00:16.
 */
@RunWith(AndroidRUnit4ClassRunner.class)
public class BaseTest {

    private static final String TAG = "CNBLOGS-API-TEST";

    protected Context mContext;

    @Before
    public void setup() {
        mContext = InstrumentationRegistry.getTargetContext();
//        DbCnblogs.init(mContext);
    }

    protected CnblogsApiProvider getApiProvider() {
        return CnblogsApiFactory.getInstance(mContext);
    }

    protected <T> void runTest(final String testName, Observable<T> observable) {
        observable.subscribeOn(Schedulers.io())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        AndroidRUnit4ClassRunner.finish(testName);
                    }
                })
                .subscribe(new DefaultObserver<T>() {
                    @Override
                    public void onNext(T t) {
                        if (t instanceof String) {
                            Log.i(TAG, t.toString());
                            return;
                        }
                        RUnitTestLogUtils.print(TAG, t);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "[" + testName + "] 测试失败", e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "[" + testName + "] 测试完成");
                    }
                });
    }
}
