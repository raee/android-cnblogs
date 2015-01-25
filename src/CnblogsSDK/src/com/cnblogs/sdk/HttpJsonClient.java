package com.cnblogs.sdk;

import org.apache.http.Header;

import android.util.Log;

import com.rae.core.http.async.AsyncHttpClient;
import com.rae.core.http.async.BinaryHttpResponseHandler;
import com.rae.core.http.async.RequestParams;
import com.rae.core.http.async.TextHttpResponseHandler;

/**
 * HttpJson下载器
 * 
 * @author ChenRui
 * 
 */
class HttpJsonClient {

	private AsyncHttpClient		httpClient;
	private int					timeout	= 5000; // 默认5秒超时
	private CnblogUiListener	mListener;

	HttpJsonClient(CnblogUiListener listener) {
		httpClient = new AsyncHttpClient();
		httpClient.setTimeout(timeout);
		this.mListener = listener;
	}

	private void notifyError(CnblogUiError error) {
		if (mListener != null) {
			mListener.onError(error);
		}
	}

	private void notifyLoadding(int progress, int totalSize) {
		if (mListener != null) {
			mListener.onLoadding(progress, totalSize);
		}
	}

	private void notifySuccess(String json) {
		if (mListener != null) {
			mListener.onSuccess(json);
		}
	}

	/**
	 * 获取JSON
	 * 
	 * @param url
	 *            路径
	 * @param params
	 *            参数
	 */
	public void getJson(String url, RequestParams params) {
		httpClient.get(url, params, new HttpJsonResponseHandler());
	}

	public void getJson(String url) {
		getJson(url, null);
	}

	/**
	 * Http 响应
	 * 
	 * @author ChenRui
	 * 
	 */
	class HttpJsonResponseHandler extends TextHttpResponseHandler {

		@Override
		public void onCancel() {
			notifyError(new CnblogUiError("1000", "HTTP请求取消！"));
		}

		@Override
		public void onStart() {
			super.onStart();
			notifyLoadding(0, 0);
		}

		@Override
		public void onProgress(int bytesWritten, int totalSize) {
			Log.d("HttpJsonClient", "w:" + bytesWritten + ";tatal:" + totalSize);
			double current = bytesWritten * 0.01;
			double count = totalSize * 0.01;
			int progress = (int) ((current / count) * 100);
			notifyLoadding(progress, totalSize);
		}

		@Override
		public void onFailure(int statusCode, Header[] head, String responseString, Throwable e) {
			notifyError(new CnblogUiError(statusCode + "", Log.getStackTraceString(e)));
		}

		@Override
		public void onSuccess(int statusCode, Header[] head, String responseString) {
			if (statusCode == 200) {
				notifySuccess(responseString);
			}
			else {
				notifyError(new CnblogUiError(statusCode + "", "服务器错误!" + responseString));
			}
		}
	}
}
