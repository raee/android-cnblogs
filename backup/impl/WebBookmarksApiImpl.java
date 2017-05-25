package com.rae.cnblogs.sdk.impl;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;
import com.rae.cnblogs.sdk.api.IBookmarksApi;
import com.rae.cnblogs.sdk.bean.BookmarksBean;
import com.rae.cnblogs.sdk.parser.BookmarksParser;
import com.rae.core.Rae;
import com.rae.core.sdk.ApiUiArrayListener;
import com.rae.core.sdk.ApiUiListener;
import com.rae.core.sdk.exception.ApiErrorCode;
import com.rae.core.sdk.exception.ApiException;
import com.rae.core.sdk.net.IApiJsonResponse;

import java.util.List;

/**
 * 收藏夹接口实现
 * Created by ChenRui on 2017/1/14 13:58.
 */
public class WebBookmarksApiImpl extends CnblogsBaseApi implements IBookmarksApi {

    public WebBookmarksApiImpl(Context context) {
        super(context);
    }

    @Override
    protected <T> IApiJsonResponse getDefaultJsonResponse(Class<T> cls, ApiUiListener<T> listener) {
        return super.getDefaultJsonResponse(cls, listener);
    }

    @Override
    public void addBookmarks(BookmarksBean m, ApiUiListener<Void> listener) {

        if (isNotLogin()) {
            listener.onApiFailed(new ApiException(ApiErrorCode.ERROR_NOT_LOGIN), ApiErrorCode.ERROR_NOT_LOGIN.getMessage());
            return;
        }
        postWithJsonBody(ApiUrls.API_BOOK_MARKS_ADD,
                newParams()
                        .add("isPublic", "1")
                        .add("linkType", "1")
                        .add("summary", m.getSummary())
                        .add("title", m.getTitle())
                        .add("url", m.getLinkUrl()),
                getDefaultJsonResponse(Void.class, listener));
    }

    @Override
    public void getBookmarks(int page, ApiUiArrayListener<BookmarksBean> listener) {

        if (isNotLogin()) {
            listener.onApiFailed(new ApiException(ApiErrorCode.ERROR_NOT_LOGIN), ApiErrorCode.ERROR_NOT_LOGIN.getMessage());
            return;
        }
        get(ApiUrls.API_BOOK_MARKS_LIST.replace("@page", String.valueOf(page)), null, new BookmarksParser(listener));
    }

    @Override
    public void delBookmarks(final String url, final ApiUiListener<Void> listener) {

        if (isNotLogin()) {
            listener.onApiFailed(new ApiException(ApiErrorCode.ERROR_NOT_LOGIN), ApiErrorCode.ERROR_NOT_LOGIN.getMessage());
            return;
        }

        // Web接口需要的是linkId，为了方便我们去获取列表的第一条数据跟URL做对比获取他的ID
        getBookmarks(1, new ApiUiArrayListener<BookmarksBean>() {
            @Override
            public void onApiFailed(ApiException e, String s) {
                listener.onApiFailed(e, s);
            }

            @Override
            public void onApiSuccess(List<BookmarksBean> bookmarksBeen) {
                if (Rae.isEmpty(bookmarksBeen) || !TextUtils.equals(url, bookmarksBeen.get(0).getLinkUrl())) {
                    listener.onApiFailed(new ApiException(ApiErrorCode.ERROR_EMPTY_DATA), "没有找到要删除的收藏");
                    return;
                }
                delBookmarksByLinkId(String.valueOf(bookmarksBeen.get(0).getWzLinkId()), listener);
            }
        });

    }


    /**
     * 根据linkId上传收藏
     *
     * @param linkId
     * @param listener
     */
    private void delBookmarksByLinkId(String linkId, final ApiUiListener<Void> listener) {

        if (isNotLogin()) {
            listener.onApiFailed(new ApiException(ApiErrorCode.ERROR_NOT_LOGIN), ApiErrorCode.ERROR_NOT_LOGIN.getMessage());
            return;
        }

        xmlHttpRequestWithJsonBody(ApiUrls.API_BOOK_MARKS_DELETE,
                newParams()
                        .add("linkId", linkId),
                new IApiJsonResponse() {
                    @Override
                    public void onJsonResponse(String s) {
                        if (TextUtils.equals(s, "1")) {
                            // 成功
                            listener.onApiSuccess(null);
                        } else {
                            listener.onApiFailed(new ApiException(ApiErrorCode.ERROR_EMPTY_DATA), s);
                        }
                    }

                    @Override
                    public void onJsonResponseError(int i, Throwable throwable) {
                        VolleyError error = (VolleyError) throwable;
                        String message = new String(error.networkResponse.data);
                        Log.e("rae", message);

                        listener.onApiFailed(new ApiException(ApiErrorCode.ERROR_EMPTY_DATA, throwable), ApiErrorCode.ERROR_SERVER.getMessage());
                    }
                });
    }
}
