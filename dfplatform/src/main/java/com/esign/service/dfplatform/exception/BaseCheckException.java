package com.esign.service.dfplatform.exception;

/**
 * 参数校验异常类
 *
 * @author houlandong
 *
 */
public class BaseCheckException extends RuntimeException {

	private static final long serialVersionUID = -5177249013876137693L;

	/**
	 * 自定义异常信息
	 * 
	 * @param message
	 */
	public BaseCheckException(String message) {
		super(message);
	}
}
