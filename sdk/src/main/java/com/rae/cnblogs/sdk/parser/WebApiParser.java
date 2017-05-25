//package com.rae.cnblogs.sdk.parser;
//
//import android.text.TextUtils;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONException;
//import com.alibaba.fastjson.JSONObject;
//
//import org.jsoup.Jsoup;
//
///**
// * 网页版的接口默认JSON解析
// * Created by ChenRui on 2017/1/22 0022 10:18.
// */
//public class WebApiParser<T> implements IHtmlParser<T> {
//
//
//    @Override
//    public T parse(String json) {
//
//        // 删除评论的时候会返回true or false
//        if (TextUtils.equals(json, "true")) {
//            // 通知成功
//            return null;
//        }
//
//        if (TextUtils.equals(json, "false")) {
//            // 通知失败
//            return null;
//        }
//
////        if (mClass == Void.class && parseJson(json) == null) {
//////            notifyApiSuccess(null);
////            return;
////        }
////
//        // 解析公共部分
//        try {
//            if (json.contains("用户登录")) {
//                // 没有登录
//                return null;
//            }
//
//            JSONObject obj = JSON.parseObject(json);
//            boolean isSuccess = false;
//            String message = null;
//            Object data = null;
//            if (obj.containsKey("IsSuccess")) {
//                isSuccess = obj.getBoolean("IsSuccess");
//            }
//            if (obj.containsKey("IsSucceed")) {
//                isSuccess = obj.getBoolean("IsSucceed");
//            }
//            if (obj.containsKey("Message")) {
//                message = obj.getString("Message");
//            }
//            if (obj.containsKey("Data")) {
//                data = obj.get("Data");
//            }
//            if (isSuccess && data != null) {
//                // 解析JSON数据
//                json = data.toString();
//            } else if (isSuccess && mClass == Void.class) {
//                notifyApiSuccess(null);
//                return null;
//            } else {
//                message = Jsoup.parse(message).text();
//                // 通知失败
//                return null;
//            }
//        } catch (JSONException ex) {
//            ex.printStackTrace();
//            // 数据解析异常
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            // 异常
//        }
//
//        return null;
//    }
//}
