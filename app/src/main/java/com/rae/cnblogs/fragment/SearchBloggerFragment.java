package com.rae.cnblogs.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.rae.cnblogs.message.SearchEvent;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.api.ISearchApi;
import com.rae.cnblogs.sdk.bean.UserInfoBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import io.reactivex.Observable;

/**
 * 博主搜索
 * Created by ChenRui on 2017/8/29 0029 14:27.
 */
public class SearchBloggerFragment extends BloggerFragment {

    ISearchApi mSearchApi;
    private String mSearchText;

    public static SearchBloggerFragment newInstance() {
        return new SearchBloggerFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSearchApi = CnblogsApiFactory.getInstance(getContext()).getSearchApi();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected boolean canAutoStart() {
        return false;
    }

    @Override
    protected Observable<List<UserInfoBean>> getFollowAndFansList() {
        return mSearchApi.searchBlogAuthor(mSearchText);
    }

    @Subscribe
    public void onEvent(SearchEvent event) {
        mPlaceholderView.loading("正在搜索..");
        mSearchText = event.getSearchText();
        start();
    }
}
