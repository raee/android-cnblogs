package com.rae.cnblogs.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.SeekBar;
import android.widget.TextView;

import com.rae.cnblogs.R;

import butterknife.BindView;

/**
 * 字体设置
 * Created by ChenRui on 2017/10/12 0012 23:30.
 */
public class FontSettingActivity extends SwipeBackBaseActivity {
    @BindView(R.id.tv_message)
    TextView mMessage;
    @BindView(R.id.seekBar)
    SeekBar mSeekBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_font_setting);
        showHomeAsUp();


        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int value, boolean b) {
                switch (value) {
                    case 0:
                        mMessage.setTextSize(14);
                        break;
                    case 1:
                        mMessage.setTextSize(16);
                        break;
                    case 2:
                        mMessage.setTextSize(18);
                        break;
                    case 3:
                        mMessage.setTextSize(24);
                        break;
                    case 4:
                        mMessage.setTextSize(26);
                        break;
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
