package com.rae.cnblogs;


public class BlogException extends Exception
{
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 7645007851445714970L;
	
	private int					mCode;
	
	public BlogException()
	{
	}
	
	public BlogException(String msg)
	{
		super(msg);
	}
	
	public BlogException(Throwable e)
	{
		super(e);
	}
	
	public BlogException(int code, Throwable e)
	{
		super(e);
		setCode(code);
	}
	
	public BlogException(String msg, Throwable e)
	{
		super(msg, e);
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
