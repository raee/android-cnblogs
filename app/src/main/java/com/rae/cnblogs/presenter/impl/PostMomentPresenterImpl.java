package com.rae.cnblogs.presenter.impl;

import android.content.Context;
import android.text.TextUtils;

import com.rae.cnblogs.presenter.IPostMomentContract;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.Empty;
import com.rae.cnblogs.sdk.api.IMomentApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 发布闪存
 * Created by ChenRui on 2017/10/27 0027 14:37.
 */
public class PostMomentPresenterImpl extends BasePresenter<IPostMomentContract.View> implements IPostMomentContract.Presenter {

    IMomentApi mMomentApi;

    public PostMomentPresenterImpl(Context context, IPostMomentContract.View view) {
        super(context, view);
        mMomentApi = getApiProvider().getMomentApi();
    }

    @Override
    public boolean post() {
        String content = mView.getContent();
        if (TextUtils.isEmpty(content)) {
            return false;
        }

        if (content.length() > 300) {
            mView.onPostMomentFailed("请精简一下内容，不要超过300字");
            return false;
        }

//        content = withImageContent(content, mView.getImageUrls());

        // 现阶段只能发布公开内容
        createObservable(mMomentApi.publish(content, 1))
                .subscribe(new ApiDefaultObserver<Empty>() {
                    @Override
                    protected void onError(String message) {
                        mView.onPostMomentFailed(message);
                    }

                    @Override
                    protected void accept(Empty empty) {
                        mView.onPostMomentSuccess();
                    }
                });

        return true;
    }

    private String withImageContent(String content, List<String> images) {
        // 格式：内容 @data客户端识别的JSON
        StringBuilder sb = new StringBuilder(content.trim());
        sb.append(" "); // 加空格
        sb.append("@data");

        try {
            JSONObject obj = new JSONObject();
            obj.put("from", "android");
            obj.put("package", "com.rae.cnblogs");
            JSONArray array = new JSONArray();
            for (String image : images) {
                array.put(image);
            }
            obj.putOpt("images", array);
            sb.append(obj.toString());
            return sb.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return content;
    }
}
