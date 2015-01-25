package com.cnblogs.sdk;

/**
 * 博客园错误信息
 * 
 * @author ChenRui
 * 
 */
public class CnblogUiError {
	private String	errorCode;
	private String	message;

	public CnblogUiError() {
	}

	public CnblogUiError(String errorCode, String message) {
		setErrorCode(errorCode);
		setMessage(message);
	}

	public CnblogUiError(String message) {
		setErrorCode("0000");
		setMessage(message);
	}

	/**
	 * 获取错误代码
	 * 
	 * @return
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * 获取错误消息
	 * 
	 * @return
	 */
	public String getMessage() {
		return message;
	}

	void setMessage(String message) {
		this.message = message;
	}

	void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	@Override
	public String toString() {
		return "[" + getErrorCode() + "] - " + getMessage();
	}
}
