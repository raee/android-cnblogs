package rae.com.cnblogs.sdk;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.Log;
import android.webkit.CookieManager;

import com.github.raee.runit.AndroidRUnit4ClassRunner;
import com.github.raee.runit.RUnitTestLogUtils;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.CnblogsApiProvider;
import com.rae.cnblogs.sdk.UserProvider;

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
        UserProvider.init(mContext);
//        DbCnblogs.init(mContext);
        // 模拟已经登录, 需要.CNBlogsCookie和.Cnblogs.AspNetCore.Cookies
        String url = "www.cnblogs.com";
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        cookieManager.setCookie(url, ".CNBlogsCookie=0922AD7D418324B66C0A8A363EB224DA99247FA6A4C6CC60BC6FAB2487D35CE07224CA0CFC44A98B4691A9474AFB12559B3A8B881EABBCF76F9C3BE44295739B4F38D9F382ECE1B8FD2217CF4D731C81977DD9B0; domain=.cnblogs.com; expires=Fri, 07-Jul-2017 11:01:57 GMT; path=/; HttpOnly");
        cookieManager.setCookie(url, ".Cnblogs.AspNetCore.Cookies=CfDJ8PhlBN8IFxtHhqIV3s0LCDlIlVzo-JtdoAJZpM5efI0M21VjySKRmE_LqL1kpnDotIQHtbrxOzGSkWWDRHathr9hmjPlV7M-FwiXaUJbXp9UU2jTEk3iMLmCL3gzIHHbKQ24nFj3SxD4JyIPfjuGnTo8ZzPJoLACtNEat1UDOQet8M3rdkMogzJNxtnr8ZhsikiMisKIdb1YzZ3jNX2lw2KD9GQxG2oFDtsIv-w0Y5JlJCZ_2G2Y9YRS1OcI5OKhN_RNLmY3c8aVe9Q0ajrmGLqmDBP4HaQk_u3r5Uk-SQpWvddw262cAo9l85rlQ8b1Aw; domain=.cnblogs.com; expires=Fri, 07-Jul-2017 11:01:57 GMT; path=/; HttpOnly");
        cookieManager.flush();

        String cookie = cookieManager.getCookie(url);

        Log.i("rae", cookie == null ? "empty" : cookie);
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
