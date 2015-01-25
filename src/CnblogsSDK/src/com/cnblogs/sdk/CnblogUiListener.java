package com.cnblogs.sdk;

/**
 * 博客园回调接口
 * 
 * @author ChenRui
 * 
 */
public interface CnblogUiListener {

	/**
	 * 接口发生错误时回调
	 * 
	 * @param error
	 *            错误信息
	 */
	public void onError(CnblogUiError error);

	/**
	 * 接口调用成功返回JSON数据。
	 * 
	 * @param json
	 *            JSON数据，使用{@link CnblogJsonFactory} 解析成想要的数据。
	 */
	public void onSuccess(String json);

	/**
	 * HTTP请求状态回调
	 * 
	 * @param progress
	 *            当前进度返回百分比
	 * @param totalSize
	 *            数据总大小
	 */
	public void onLoadding(int progress, int totalSize);

}
