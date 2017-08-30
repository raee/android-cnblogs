package com.rae.cnblogs.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.Shape;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;

/**
 * 开关按钮
 * Created by ChenRui on 2017/8/31 0031 0:23.
 */
public class RaeSwitchCheckBox extends AppCompatCheckBox {
    private final Paint mOvalPaint = new Paint();
    private final RectF mOvalRect = new RectF();

    public RaeSwitchCheckBox(Context context) {
        super(context);
        initViews();
    }

    public RaeSwitchCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public RaeSwitchCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    Shape shape = new OvalShape();

    private void initViews() {
        mOvalPaint.setAntiAlias(true);
        mOvalPaint.setColor(Color.GRAY);

        shape.resize(100, 100);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

//        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);

        shape.draw(canvas, mOvalPaint);
    }
}
