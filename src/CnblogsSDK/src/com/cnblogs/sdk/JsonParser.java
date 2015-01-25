package com.cnblogs.sdk;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

abstract class JsonParser<T> {

	/**
	 * 解析Json数据
	 * 
	 * @param json
	 *            Json字符串
	 * @return
	 */
	public abstract List<T> parser(String json);

	/**
	 * 获取Json返回的数据
	 * 
	 * <pre>
	 * 返回结果都是data,op的格式可采用该方法先解析
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	protected JSONArray getJsonData(String json) throws JSONException {
		JSONObject obj = new JSONObject(json);
		return obj.getJSONArray("data");
	}
}
