//package com.rae.cnblogs.sdk.impl;
//
//import android.content.Context;
//
//import com.rae.cnblogs.sdk.IBookmarksApi;
//import com.rae.cnblogs.sdk.bean.BookmarksBean;
//import com.rae.core.sdk.ApiParams;
//import com.rae.core.sdk.ApiUiArrayListener;
//import com.rae.core.sdk.ApiUiListener;
//import com.rae.core.sdk.net.ApiRequest;
//
//import java.net.URLEncoder;
//
///**
// * 收藏夹接口实现
// * Created by ChenRui on 2017/1/14 13:58.
// */
//public class BookmarksApiImpl extends CnblogsBaseApi implements IBookmarksApi {
//
//    public BookmarksApiImpl(Context context) {
//        super(context);
//    }
//
//    @Override
//    public void addBookmarks(BookmarksBean m, ApiUiListener<Void> listener) {
//
//        // http 返回409 表示已经添加过了
//        post(ApiUrls.OFFICIAL_API_BOOK_MARKS_ADD,
//                newParams()
//                        .add("Title", m.getTitle())
//                        .add("LinkUrl", m.getLinkUrl())
//                        .add("Summary", m.getSummary()),
//                Void.class,
//                listener
//        );
//    }
//
//    @Override
//    public void getBookmarks(int page, ApiUiArrayListener<BookmarksBean> listener) {
//        get(ApiUrls.OFFICIAL_API_BOOK_MARKS_LIST,
//                newParams().add("pageIndex", page).add("pageSize", 20),
//                BookmarksBean.class,
//                listener);
//    }
//
//    @Override
//    public void delBookmarks(String url, ApiUiListener<Void> listener) {
//        String requestUrl;
//        ApiParams apiParams = newParams();
//        if (url.startsWith("http")) {
//            requestUrl = ApiUrls.OFFICIAL_API_BOOK_MARKS_URL_DELETE;
//            apiParams.add("url", URLEncoder.encode(url));
//        } else {
//            requestUrl = ApiUrls.OFFICIAL_API_BOOK_MARKS_ID_DELETE + "/" + url;
//        }
//
//        ApiRequest request = newDelRequestBuilder(requestUrl, apiParams).listener(getDefaultJsonResponse(Void.class, listener)).build();
//        sendRequest(request);
//    }
//}
