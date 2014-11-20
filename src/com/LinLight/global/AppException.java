package com.LinLight.global;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.HttpHostConnectException;

import com.LinLight.util.StringUtil;

/**
 * 描述： 公共异常类.
 */
public class AppException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1;

	
	/** 异常消息. */
	private String msg = null;

	/**
	 * 构造异常类.
	 *
	 * @param e 异常
	 */
	public AppException(Exception e) {
		super();

		try {
			if( e instanceof HttpHostConnectException) {  
				msg = Constant.UNKNOWNHOSTEXCEPTION;
			}else if (e instanceof ConnectException) {
				msg = Constant.CONNECTEXCEPTION;
			}else if (e instanceof UnknownHostException) {
				msg = Constant.UNKNOWNHOSTEXCEPTION;
			}else if (e instanceof SocketException) {
				msg = Constant.SOCKETEXCEPTION;
			}else if (e instanceof SocketTimeoutException) {
				msg = Constant.SOCKETTIMEOUTEXCEPTION;
			}else if( e instanceof NullPointerException) {  
				msg = Constant.NULLPOINTEREXCEPTION;
			}else if( e instanceof ClientProtocolException) {  
				msg = Constant.CLIENTPROTOCOLEXCEPTION;
			}else {
				if (e == null || StringUtil.isEmpty(e.getMessage())) {
					msg = Constant.NULLMESSAGEEXCEPTION;
				}else{
				    msg = e.getMessage();
				}
			}
		} catch (Exception e1) {
		}
		
	}

	/**
	 * 用一个消息构造异常类.
	 *
	 * @param message 异常的消息
	 */
	public AppException(String message) {
		super(message);
		msg = message;
	}

	/**
	 * 描述：获取异常信息.
	 *
	 * @return the message
	 */
	@Override
	public String getMessage() {
		return msg;
	}

}
