package com.rae.cnblogs.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;

/**
 * 截图工具
 * Created by ChenRui on 2017/10/10 0010 15:59.
 */
public final class ViewCaptureUtils {

    private WeakReference<Context> mContextWeakReference;

    public ViewCaptureUtils(Context context) {
        mContextWeakReference = new WeakReference<>(context);
    }

    public Observable<File> capture(@NonNull WebView view, @Nullable final String path) {
        return Observable.just(view)
                .map(new Function<WebView, Bitmap>() {
                    @Override
                    public Bitmap apply(WebView view) throws Exception {
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                            return convertViewToBitmapV19(view);
                        }
                        return convertViewToBitmap(view);
                    }
                })
                .map(new Function<Bitmap, File>() {
                    @Override
                    public File apply(Bitmap bitmap) throws Exception {
                        File file;
                        if (TextUtils.isEmpty(path)) {
                            // 生成临时文件
                            String fileName = "temp" + System.currentTimeMillis() + ".jpg";
                            if (mContextWeakReference.get() != null) {
                                file = new File(mContextWeakReference.get().getExternalCacheDir(), fileName);
                            } else {
                                file = new File(Environment.getDataDirectory(), fileName);
                            }
                        } else {
                            file = new File(path);
                        }

                        if (file.exists()) {
                            boolean res = file.delete();
                            Log.d("rae", "删除文件：" + file.getPath() + ";是否成功：" + res);
                        }

                        FileOutputStream os = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, os);
                        os.flush();
                        os.close();
                        return file;
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }

//    // 5.0 方式
//    private Bitmap convertViewToBitmap(WebView view) {
//        int height = (int) (view.getContentHeight() * view.getScale()); // 内容高度
//        if (height <= 0) return null;
//        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), height, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        view.draw(canvas);
//        return bitmap;
//    }

    /**
     * API 19 以下的
     *
     * @param view
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private Bitmap convertViewToBitmapV19(WebView view) {
        // 获取Picture对象
        Picture picture = view.capturePicture();
        // 得到图片的宽和高（没有reflect图片内容）
        int width = picture.getWidth();
        int height = picture.getHeight();
        if (width > 0 && height > 0) {
            // 创建位图
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            picture.draw(canvas);
            return bitmap;
        }
        return null;
    }

    private Bitmap convertViewToBitmap(WebView view) {

        int height = (int) (view.getContentHeight() * view.getScale()); // 内容高度
        if (height <= 0) return null;

        // 滚动截图
        view.setEnabled(false);
        // 记录当前位置
        int srcX = view.getScrollX();
        int srcY = view.getScrollY();

        // 滚动到头部
        view.scrollTo(0, 0);
        int y = view.getScrollY();
        int h = view.getHeight(); // 屏幕高度

        // 拼接图像
        Bitmap result = Bitmap.createBitmap(view.getMeasuredWidth(), height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawColor(Color.WHITE);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);

        Log.w("rae", "截图高度：" + height + "; 屏幕高度：" + h);
        do {
            view.scrollTo(0, y);
            Log.i("rae", "正在截图：" + y);
            Bitmap bmp = capturePicture(view);
            if (bmp == null) {
                Log.w("rae", "没有图像：" + y);
                continue;
            }
            canvas.drawBitmap(bmp, 0, y, paint);
            if (y == height) {
                break;
            }
            y += h;
            y = Math.min(height, y);
            Log.w("rae", "下一个截图---> " + y);
        } while (y <= height);

        // 还原位置
        view.scrollTo(srcX, srcY);

        return result;
    }

    private Bitmap capturePicture(WebView v) {

        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);

        // Reset the drawing cache background color to fully transparent
        // for the duration of this operation
        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(0);

        if (color != 0) {
            v.destroyDrawingCache();
        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = v.getDrawingCache();
        if (cacheBitmap == null) {
            return null;
        }

        // 压缩图片
//        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

        // Restore the view
        v.destroyDrawingCache();
        v.setWillNotCacheDrawing(willNotCache);
        v.setDrawingCacheBackgroundColor(color);

        return cacheBitmap;
    }
}
