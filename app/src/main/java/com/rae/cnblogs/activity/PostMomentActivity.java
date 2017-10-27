package com.rae.cnblogs.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.AppUI;
import com.rae.cnblogs.R;
import com.rae.cnblogs.presenter.CnblogsPresenterFactory;
import com.rae.cnblogs.presenter.IPostMomentContract;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 发布闪存
 * Created by ChenRui on 2017/10/27 0027 14:04.
 */
public class PostMomentActivity extends BaseActivity implements IPostMomentContract.View {
    @BindView(R.id.et_content)
    EditText mContentView;
    @BindView(R.id.tv_post)
    TextView mPostView;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private IPostMomentContract.Presenter mPresenter;
    private PostImageAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_moment);
        ButterKnife.bind(this);
        showHomeAsUp();
        mPresenter = CnblogsPresenterFactory.getPostMomentPresenter(this, this);
        mRecyclerView.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mAdapter = new PostImageAdapter();
        mRecyclerView.setAdapter(mAdapter);

        AppRoute.jumpToImageSelection(this, null);
    }

    @Override
    protected int getHomeAsUpIndicator() {
        return R.drawable.ic_back_closed;
    }

    @Override
    public String getContent() {
        return mContentView.getText().toString();
    }

    @Override
    public void onPostMomentFailed(String msg) {
        mPostView.setEnabled(true);
        AppUI.dismiss();
        AppUI.failed(this, msg);
    }

    @Override
    public void onPostMomentSuccess() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public List<String> getImageUrls() {
        return null;
    }

    @OnClick(R.id.tv_post)
    public void onPostViewClick() {

//        Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/raetest.jpg");
//        mImageView.setImageBitmap(bitmap);
//
//        RequestBody body = MultipartBody.create(null, new File("/sdcard/raetest.jpg"));
//        MultipartBody.Part file = MultipartBody.Part.create(body);
//
//        RxObservable.create(postApi.uploadImage("rae.jpg", "rae.jpg", body), "test").subscribe(new ApiDefaultObserver<String>() {
//            @Override
//            protected void onError(String message) {
//                AppUI.failed(getContext(), message);
//            }
//
//            @Override
//            protected void accept(String url) {
//                AppUI.toastInCenter(getContext(), "上传成功！" + url);
//            }
//        });

        if (mPresenter.post()) {
            AppUI.loading(this, "正在发布");
            mPostView.setEnabled(false);
        }
    }

    private static class PostImageHolder extends RecyclerView.ViewHolder {

        public PostImageHolder(View itemView) {
            super(itemView);
        }
    }

    private static class PostImageAdapter extends RecyclerView.Adapter<PostImageHolder> {

        private final List<String> mUrls = new ArrayList<>();
        private LayoutInflater mLayoutInflater;

        public PostImageAdapter() {
            super();
        }

        @Override
        public PostImageHolder onCreateViewHolder(ViewGroup parent, int i) {
            if (mLayoutInflater == null) {
                mLayoutInflater = LayoutInflater.from(parent.getContext());
            }
            return new PostImageHolder(mLayoutInflater.inflate(R.layout.item_post_moment_image, parent, false));
        }

        @Override
        public void onBindViewHolder(PostImageHolder postImageHolder, int i) {

        }

        @Override
        public int getItemCount() {
            return mUrls.size() + 1;
        }

        public void addImage(String fileName) {
            mUrls.add(0, fileName);
        }

        public void remove(String fileName) {
            mUrls.remove(fileName);
        }
    }
}
