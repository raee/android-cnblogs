package com.rae.cnblogs.dialog.impl;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;

import com.rae.cnblogs.AppUI;
import com.rae.cnblogs.R;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.IBlogApi;
import com.rae.cnblogs.sdk.bean.Blog;
import com.rae.cnblogs.sdk.bean.BlogComment;
import com.rae.core.sdk.ApiUiListener;
import com.rae.core.sdk.exception.ApiException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 写评论
 * Created by ChenRui on 2017/1/31 23:17.
 */
public class EditCommentDialog extends SlideDialog implements ApiUiListener<Void> {

    public interface OnEditCommentListener {
        void onSendCommentSuccess(String body);
    }

    private final Blog mBlog;
    @BindView(R.id.et_edit_comment_body)
    EditText mBodyView;

    @BindView(R.id.cb_ref_comment)
    CheckBox mReferenceView;

    private BlogComment mBlogComment;

    private IBlogApi mBlogApi;

    private OnEditCommentListener mOnEditCommentListener;


    public EditCommentDialog(Context context, Blog blog) {
        super(context);
        mBlog = blog;
        mBlogApi = CnblogsApiFactory.getInstance(context).getBlogApi();
        setContentView(R.layout.dialog_blog_comment_edit);
        ButterKnife.bind(this);
    }

    @Override
    protected void onWindowLayout(Window window, WindowManager.LayoutParams attr) {
        super.onWindowLayout(window, attr);
        window.setDimAmount(0.5f);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public void show() {
        super.show();
        mReferenceView.setVisibility(View.GONE);
        mBodyView.setText("");
    }

    public void show(BlogComment comment) {
        setBlogComment(comment);
        show();
        mReferenceView.setVisibility(View.VISIBLE);
        mReferenceView.setChecked(true);
        if (comment != null) {
            mReferenceView.setText("引用@" + comment.getAuthorName() + "的评论");
            mBodyView.setHint("回复：“" + comment.getBody() + "”");
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        mBodyView.setText("");
        mBlogComment = null;
    }

    public void setBlogComment(BlogComment blogComment) {
        mBlogComment = blogComment;
    }

    public void setOnEditCommentListener(OnEditCommentListener onEditCommentListener) {
        mOnEditCommentListener = onEditCommentListener;
    }

    @OnClick(R.id.btn_send_comment)
    void onSendClick() {
        String content = mBodyView.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            return;
        }

        AppUI.loading(getContext(), "正在发表...");
        if (mBlogComment == null) {
            mBlogApi.addBlogComment(mBlog.getBlogId(), mBlog.getBlogApp(), "", content, this);
        } else {
            // 引用评论
            if (mReferenceView.isChecked()) {
                mBlogApi.addBlogComment(mBlog.getBlogId(), mBlog.getBlogApp(), mBlogComment, content, this);
            } else {
                mBlogApi.addBlogComment(mBlog.getBlogId(), mBlog.getBlogApp(), mBlogComment.getId(), content, this);
            }
        }
    }

    @Override
    public void onApiFailed(ApiException e, String s) {
        AppUI.dismiss();
        dismiss();
        AppUI.toast(getContext(), s);
    }

    @Override
    public void onApiSuccess(Void aVoid) {
        AppUI.dismiss();
        AppUI.toastInCenter(getContext(), "评论发表成功！");
        if (mOnEditCommentListener != null) {
            mOnEditCommentListener.onSendCommentSuccess(mBodyView.getText().toString().trim());
        }
        dismiss();
    }
}
