package com.rae.cnblogs.sdk.service.task;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.config.CnblogSdkConfig;
import com.rae.cnblogs.sdk.config.OfflineConfig;

/**
 * 博客服务的任务，采用装饰者设计模式
 * Created by ChenRui on 2017/2/1 17:33.
 */
public abstract class BlogServiceTask extends Thread implements IServiceTask {

    protected final OfflineConfig mConfig;
    private final IServiceTask mParentTask;
    protected Context mContext;
    private String TAG = "BlogServiceTask";
    private OnTaskFinishListener mListener;
    private boolean mIsFinish;


    public BlogServiceTask(Context context, IServiceTask task) {
        mContext = context;
        mParentTask = task;
        mConfig = CnblogSdkConfig.getsInstance(mContext).getOfflineConfig();
    }

    /**
     * 正式开始执行任务
     */
    protected abstract void runTask();

    protected boolean isWIFI() {
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
        }

        return false;
    }

    @Override
    public void setOnTaskFinishListener(OnTaskFinishListener listener) {
        mListener = listener;
    }

    @Override
    public void run() {
        super.run();
        Log.i(TAG, "开始执行任务：" + getTaskName());
        runTask();

        mIsFinish = true;

        // 来到这里任务就完成了
        if (mListener != null) {
            mListener.onTaskFinish(getTaskName());
        }

        Log.i(TAG, "完成任务：" + getTaskName());
    }

    // 开始自己的任务
    private void startSelfTask() {
        super.start();
        mIsFinish = false;
    }

    @Override
    public void startTask() {
        // 配置了不启用离线任务
        if (!mConfig.isEnableOffline()) {
            Log.e(TAG, "用户配置了不启动离线任务！任务取消：" + getTaskName());
            return;
        }

        if (!isWIFI()) {
            Log.e(TAG, "当前不是WIFI环境！不启动任务：" + getTaskName());
            return;
        }

        // 没有装饰者，启动自己的任务
        if (mParentTask == null) {
            startSelfTask();
            return;
        }

        // 先启动被装饰者的任务
        mParentTask.startTask();

        // 等待被装饰者任务执行完成
        mParentTask.setOnTaskFinishListener(new OnTaskFinishListener() {
            @Override
            public void onTaskFinish(String taskName) {
                // 开始自己的任务
                startSelfTask();
            }
        });

    }

    @Override
    public void stopTask() {
        CnblogsApiFactory.getInstance(mContext).cancel();
        mIsFinish = true; // 通知停止任务
    }

    @Override
    public boolean isFinish() {
        return mIsFinish;
    }
}
