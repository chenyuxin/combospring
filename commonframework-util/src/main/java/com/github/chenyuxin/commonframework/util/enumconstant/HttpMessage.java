package com.github.chenyuxin.commonframework.util.enumconstant;

import java.util.HashMap;
import java.util.Map;

/**
 * http消息
 * 响应默认消息
 */
public enum HttpMessage {
	
	CONTINUE(100, ""),
	SWITCHING_PROTOCOLS(101, ""),
	OK(200, "SUCCESS"),
	CREATED(201, ""),
	ACCEPTED(202, ""),
	NON_AUTHORITATIVE_INFORMATION(203, ""),
	NO_CONTENT(204, ""),
	RESET_CONTENT(205, ""),
	PARTIAL_CONTENT(206, ""),
	MULTIPLE_CHOICES(300, ""),
	MOVED_PERMANENTLY(301, ""),
	MOVED_TEMPORARILY(302, ""),
	FOUND(302, ""),
	SEE_OTHER(303, ""),
	NOT_MODIFIED(304, ""),
	USE_PROXY(305, ""),
	TEMPORARY_REDIRECT(307, ""),
	BAD_REQUEST(400, "错误请求"),
	UNAUTHORIZED(401, ""),
	PAYMENT_REQUIRED(402, ""),
	FORBIDDEN(403, ""),
	NOT_FOUND(404, ""),
	METHOD_NOT_ALLOWED(405, ""),
	NOT_ACCEPTABLE(406, ""),
	PROXY_AUTHENTICATION_REQUIRED(407, ""),
	REQUEST_TIMEOUT(408, ""),
	CONFLICT(409, ""),
	GONE(410, ""),
	LENGTH_REQUIRED(411, ""),
	PRECONDITION_FAILED(412, ""),
	REQUEST_ENTITY_TOO_LARGE(413, ""),
	REQUEST_URI_TOO_LONG(414, ""),
	UNSUPPORTED_MEDIA_TYPE(415, ""),
	REQUESTED_RANGE_NOT_SATISFIABLE(416, ""),
	EXPECTATION_FAILED(417, ""),
	INTERNAL_SERVER_ERROR(500, "系统出现错误，请重试或联系平台管理员."),
	NOT_IMPLEMENTED(501, ""),
	BAD_GATEWAY(502, ""),
	SERVICE_UNAVAILABLE(503, ""),
	GATEWAY_TIMEOUT(504, ""),
	HTTP_VERSION_NOT_SUPPORTED(505, ""),
	SEESION_EXPIRED(506, ""),
	
	;
	
	private HttpMessage(int status,String message) {
		this.status = status;
		this.message = message;
	}
	
	/**
	 * 状态
	 */
	private int status;
	
	/**
	 * 消息
	 */
	private String message;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public <T> Map<String,Object> responseMsg(T data) {
		Map<String, Object> responseMsg = new HashMap<String, Object>();
		responseMsg.put("status", status);
		responseMsg.put("message", message);
		if (null != data 
//				&& !StringPool.BLANK.equals(data)
				&& this.equals(OK)
				) {
			responseMsg.put("data", data);
		} else {
			responseMsg.put("message", data);
		}
		return responseMsg;
	}
	
	public <T> Map<String,Object> responseMsg() {
		Map<String, Object> responseMsg = new HashMap<String, Object>();
		responseMsg.put("status", status);
		responseMsg.put("message", message);
		return responseMsg;
	}
	
	
	
	
 
}
