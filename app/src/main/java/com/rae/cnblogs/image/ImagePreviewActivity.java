package com.rae.cnblogs.image;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.rae.cnblogs.R;
import com.rae.cnblogs.activity.BaseActivity;
import com.rae.cnblogs.fragment.ImagePreviewFragment;
import com.rae.swift.app.RaeFragmentAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 图片预览
 * Created by ChenRui on 2017/2/6 0006 15:48.
 */
public class ImagePreviewActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.vp_image_preview)
    ViewPager mViewPager;

    @BindView(R.id.tv_image_preview_count)
    TextView mCountView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);
        ButterKnife.bind(this);

        RaeFragmentAdapter adapter = new RaeFragmentAdapter(getSupportFragmentManager());

        List<String> data = new ArrayList<>();

        data.add("http://img05.tooopen.com/images/20150201/sl_109938035874.jpg");
        data.add("http://img04.tooopen.com/thumbnails/20130712/x_17270713.jpg");
        data.add("http://img04.tooopen.com/thumbnails/20130701/x_20083555.jpg");
        data.add("http://img06.tooopen.com/images/20170123/tooopen_sl_197478145926.jpg");
        data.add("http://img02.tooopen.com/images/20141229/sl_107003776898.jpg");
        data.add("http://img06.tooopen.com/images/20170123/tooopen_sl_197475962817.jpg");


        for (String url : data) {
            adapter.add(ImagePreviewFragment.newInstance(url));
        }


        mViewPager.setAdapter(adapter);


        if (adapter.getCount() > 1) {
            mViewPager.addOnPageChangeListener(this);
            onPageSelected(mViewPager.getCurrentItem());
        }

    }

    @OnClick(R.id.img_back)
    public void onBackClick() {
        finish();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        position++;
        mCountView.setText(String.format(Locale.getDefault(), "%d/%d", position, mViewPager.getAdapter().getCount()));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
