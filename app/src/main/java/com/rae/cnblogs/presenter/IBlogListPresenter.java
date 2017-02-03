package com.rae.cnblogs.presenter;

import com.rae.cnblogs.sdk.bean.Blog;
import com.rae.cnblogs.sdk.bean.Category;

import java.util.List;

/**
 * 首页
 * Created by ChenRui on 2016/12/2 00:21.
 */
public interface IBlogListPresenter extends IRaePresenter {

    void loadMore();

    /**
     * 刷新数据集
     */
    void refreshDataSet();

    interface IBlogListView {

        void onLoadBlogList(int pageIndex, List<Blog> data);

        void onLoadFailed(int pageIndex, String msg);

        Category getCategory();

    }
}
