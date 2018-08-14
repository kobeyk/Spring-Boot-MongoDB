package com.appleyk.result;



public enum ResponseMessage {

	/**
	 * 成功
	 */
	OK(200,"成功"),
	
	/**
	 * 成功
	 */
	NOCHANGE(200,"未做任何修改"),
	
	/**
	 * 错误的请求
	 */
	BAD_REQUEST(400,"错误的请求"),
	
	/**
	 * 错误的请求
	 */
	NOTNULL_ID(400,"请求ID不能为空");
	
	
	
	private final int status;
	
	private final String message;
	
	ResponseMessage(int status, String message){
		this.status = status;
		this.message = message;
	}
	
	public int getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}
	
}
