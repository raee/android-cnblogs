package com.rae.cnblogs.image;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.rae.cnblogs.AppUI;
import com.rae.cnblogs.R;
import com.rae.cnblogs.activity.BaseActivity;
import com.rae.cnblogs.sdk.ApiDefaultObserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 图片预览
 * Created by ChenRui on 2017/2/6 0006 15:48.
 */
public class ImagePreviewActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    @BindView(R.id.vp_image_preview)
    ViewPager mViewPager;

    @BindView(R.id.tv_image_preview_count)
    TextView mCountView;

    @BindView(R.id.ll_bottom)
    View mBottomLayout;

    @BindView(R.id.img_back)
    View mCloseView;

    ImageAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);

        ArrayList<String> images = getIntent().getStringArrayListExtra("images");
        if (images == null || images.size() <= 0) {
            finish();
            return;
        }

        mAdapter = new ImageAdapter(this, images);
        mAdapter.setOnItemClickListener(this);
        mViewPager.setAdapter(mAdapter);

        int position = getIntent().getIntExtra("position", mViewPager.getCurrentItem());

        if (mViewPager.getAdapter().getCount() > 1) {
            mViewPager.addOnPageChangeListener(this);
        }

        mViewPager.setCurrentItem(position);

        if (position <= 0)
            onPageSelected(position);

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

    @Override
    public void onClick(View v) {
        this.finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            onDownloadClick();
        }
    }

    @OnClick(R.id.img_download)
    public void onDownloadClick() {
        // 检查权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
            return;
        }

        int item = mViewPager.getCurrentItem();
        String url = mAdapter.getItem(item);
        File file = ImageLoader.getInstance().getDiskCache().get(url);
        if (file == null || !file.exists()) {
            AppUI.failed(this, "请稍候图片加载完毕再保存");
            return;
        }

        AppUI.loading(getContext(), "正在保存");

        // 保存图片
        Observable.just(file)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        AppUI.dismiss();
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        AppUI.toastInCenter(getContext(), "保存图片成功");
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        AppUI.failed(getContext(), "保存图片失败");
                    }
                })
                .map(new Function<File, File>() {
                    @Override
                    public File apply(File file) throws Exception {
                        Log.i("rae", "文件路径：" + file.getPath());
                        MediaStore.Images.Media.insertImage(getContentResolver(), file.getPath(), file.getName(), null);
                        return copy(file);
                    }
                })
                .subscribe(new ApiDefaultObserver<File>() {
                    @Override
                    protected void onError(String message) {
                    }

                    @Override
                    protected void accept(File file) {
                        // 通知图片库刷新
                        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
                        Log.i("rae", "图片库路径：" + file.getPath());
                    }
                });


    }

    /**
     * 复制图片到图片库中去
     *
     * @param file 源图片
     */
    private File copy(File file) {
        FileOutputStream outputStream = null;
        FileInputStream stream = null;
        try {
            File target = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), file.getName());
            outputStream = new FileOutputStream(target);
            stream = new FileInputStream(file);
            int len = 0;
            byte[] buffer = new byte[128];
            while ((len = stream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            return target;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null)
                    outputStream.close();
                if (stream != null)
                    stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }

}
