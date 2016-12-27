//package com.rae.cnblogs.dialog;
//
//import android.app.Dialog;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.app.FragmentManager;
//
//import com.rae.cnblogs.R;
//import com.rae.cnblogs.sdk.bean.Blog;
//
///**
// * 评论对话框
// * Created by ChenRui on 2016/12/15 23:25.
// */
//public class BlogCommentDialog extends SlideDialogFragment {
//
//    private Blog mBlog;
//
//    public static BlogCommentDialog newInstance(Blog blog) {
//        Bundle args = new Bundle();
//        args.putParcelable("blog", blog);
//        BlogCommentDialog fragment = new BlogCommentDialog();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @NonNull
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        Dialog dialog = super.onCreateDialog(savedInstanceState);
//        dialog.setContentView(R.layout.dialog_blog_comment);
//        return dialog;
//    }
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mBlog = getArguments().getParcelable("blog");
//    }
//
//    public void show(FragmentManager manager) {
//        this.show(manager, "blogComment");
//    }
//}
