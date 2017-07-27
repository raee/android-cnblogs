//package com.rae.cnblogs.image;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.Rect;
//import android.text.TextPaint;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.util.TypedValue;
//import android.view.View;
//
//import com.github.chrisbanes.photoview.PhotoView;
//import com.nostra13.universalimageloader.core.assist.FailReason;
//import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
//import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
//
//
///**
// * 图片查看
// * Created by ChenRui on 2017/7/26 0026 0:35.
// */
//public class RaeImageView extends PhotoView  {
//
//    private int mProgress;
//    private Paint mProgressPaint;
//    private final Rect mRect = new Rect();
//    private boolean mIsLoading;
//
//    public RaeImageView(Context context) {
//        super(context);
//        init();
//    }
//
//    public RaeImageView(Context context, AttributeSet attr) {
//        super(context, attr);
//        init();
//    }
//
//    public RaeImageView(Context context, AttributeSet attr, int defStyle) {
//        super(context, attr, defStyle);
//        init();
//    }
//
//    private void init() {
//        mProgressPaint = new TextPaint();
//        mProgressPaint.setAntiAlias(true);
//        mProgressPaint.setColor(Color.WHITE);
//        mProgressPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, getResources().getDisplayMetrics()));
//    }
//
//    @Override
//    public void onLoadingStarted(String imageUri, View view) {
//        mIsLoading = true;
//        invalidate();
//    }
//
//    @Override
//    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//        mIsLoading = false;
//        invalidate();
//    }
//
//    @Override
//    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//        mIsLoading = false;
//        invalidate();
//        Log.i("rae", "完成！");
//    }
//
//    @Override
//    public void onLoadingCancelled(String imageUri, View view) {
//        mIsLoading = false;
//        invalidate();
//    }
//
//    @Override
//    public void onProgressUpdate(String imageUri, View view, int current, int total) {
//        mIsLoading = true;
//        float value = ((current * 0.1f) / (total * 0.1f)) * 100.0f;
//        mProgress = (int) value;
//        Log.i("rae", "进度：" + value);
//        invalidate();
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        if (mIsLoading) {
//            String text = mProgress + "%";
//            mProgressPaint.getTextBounds(text, 0, text.length(), mRect);
//            int x = canvas.getWidth() / 2 - mRect.width() / 2;
//            int y = canvas.getHeight() / 2 - mRect.height() / 2;
//            canvas.drawText(text, x, y, mProgressPaint);
//        }
//    }
//}
