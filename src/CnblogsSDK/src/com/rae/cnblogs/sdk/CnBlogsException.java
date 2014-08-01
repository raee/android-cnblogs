package com.rae.cnblogs.sdk;

/**
 * 博客园请求异常
 * 
 * @author ChenRui
 * 
 */
public class CnBlogsException extends Exception
{
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 7645007851445714970L;
	
	private int					mCode;
	
	public CnBlogsException()
	{
	}
	
	public CnBlogsException(String msg)
	{
		super(msg);
	}
	
	public CnBlogsException(Throwable e)
	{
		super(e);
		e.printStackTrace();
	}
	
	public CnBlogsException(int code, Throwable e)
	{
		super(e);
		setCode(code);
		e.printStackTrace();
	}
	
	public CnBlogsException(String msg, Throwable e)
	{
		super(e == null ? msg : msg + ":" + e.getMessage(), e);
		e.printStackTrace();
	}
	
	public void setCode(int code)
	{
		this.mCode = code;
	}
	
	public int getCode()
	{
		return this.mCode;
	}
	
}
