package com.rae.cnblogs.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.rae.cnblogs.R;
import com.rae.cnblogs.widget.RaeLoadMoreView;

import butterknife.BindView;

/**
 * Created by ChenRui on 2016/12/3 18:06.
 */
public class TestActivity extends BaseActivity {

    @BindView(R.id.load_more)
    RaeLoadMoreView mLoadMoreView;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            toggle();
        }
    };

    private int mCurrent = -1;

    private void toggle() {
        if (mCurrent > 27) {
            mCurrent = -1;
        }
        Log.d("rae", "当前:" + mCurrent);
        mLoadMoreView.setProgressStyle(mCurrent);
        mCurrent++;
        mHandler.sendEmptyMessageDelayed(0, 2000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mHandler.removeMessages(0);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        bindView();
//        mLoadMoreView.setProgressStyle(ProgressStyle.BallScaleMultiple);
        mLoadMoreView.setProgressStyle(ProgressStyle.BallScale);
        //mHandler.sendEmptyMessageDelayed(0, 2000);

    }


}
