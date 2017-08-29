package com.rae.cnblogs.presenter;

import java.util.List;

/**
 * 搜索
 * Created by ChenRui on 2017/8/29 0029 9:37.
 */
public class ISearchContract {

    public interface Presenter extends IAppPresenter {
        /**
         * 搜索建议
         */
        void suggest();
    }

    public interface View {

        /**
         * 搜索文本
         */
        String getSearchText();

        /**
         * 加载搜索建议成功
         */
        void onSuggestionSuccess(List<String> data);
    }
}
