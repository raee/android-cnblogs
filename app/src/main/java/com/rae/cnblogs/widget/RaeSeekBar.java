package com.rae.cnblogs.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatSeekBar;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.rae.cnblogs.R;

/**
 * 带文本的滑杆
 * Created by ChenRui on 2017/10/13 0013 12:50.
 */
public class RaeSeekBar extends AppCompatSeekBar {

    private String[] mTickMarkTitles = new String[]{
            "小号",
            "标准",
            "中号",
            "大号",
            "特大",

    };

    private final Paint mTickMarkTitlePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private float mTickMarkTitleTextSize = 18;
    private float mOffsetY = 20;
    private final Rect mRect = new Rect();

    public RaeSeekBar(Context context) {
        super(context);
        init();
    }

    public RaeSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RaeSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected void init() {
        mTickMarkTitleTextSize = getSize(mTickMarkTitleTextSize);
        mOffsetY = getSize(mOffsetY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.translate(0, mTickMarkTitleTextSize + mOffsetY);
//        canvas.save();
        super.onDraw(canvas);

        // draw line

        int width = canvas.getWidth();
        int height = canvas.getHeight();
        int h2 = height / 2;


        int max = getMax();
        int thumbStart = getPaddingLeft();
        int thumbEnd = getPaddingLeft() + width * max / max;

        mRect.left = getPaddingLeft();
        mRect.right = thumbEnd;
        mRect.top = h2 - ((int) getSize(1));
        mRect.bottom = (mRect.top + (int) getSize(2));
        canvas.drawRect(mRect, mTickMarkTitlePaint);


//        canvas.save();

//        // 总宽度
//        int width = getWidth() - getPaddingLeft() - getPaddingRight();
//
//
//        canvas.translate(0, -mTickMarkTitleTextSize);
//
//
        mTickMarkTitlePaint.setTextSize(mTickMarkTitleTextSize);
        mTickMarkTitlePaint.setTextAlign(Paint.Align.CENTER);
        mTickMarkTitlePaint.setColor(ContextCompat.getColor(getContext(), R.color.ph1));

        for (int i = 0; i <= getMax(); i++) {
            String title = mTickMarkTitles[i % mTickMarkTitles.length];
            // 每个间隔的大小
            int thumbPos = getPaddingLeft() + width * i / getMax();
            mTickMarkTitlePaint.getTextBounds(title, 0, title.length(), mRect);
//            canvas.drawText(title, thumbPos, mRect.height(), mTickMarkTitlePaint);
        }

    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 加上字体大小
        int wm = MeasureSpec.getMode(widthMeasureSpec);
        int hm = MeasureSpec.getMode(heightMeasureSpec);
        int w = getMeasuredWidth();
        int h = getMeasuredHeight();
        h += mTickMarkTitleTextSize;
        h += mOffsetY;
        // 保存
//        setMeasuredDimension(MeasureSpec.makeMeasureSpec(w, wm), MeasureSpec.makeMeasureSpec(h, hm));
    }

    protected float getSize(float size) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, getResources().getDisplayMetrics());
    }

}
