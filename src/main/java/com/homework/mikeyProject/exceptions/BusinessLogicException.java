package com.homework.mikeyProject.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class BusinessLogicException extends Exception {

	private static final long serialVersionUID = 1L;

	private HttpStatus httpStatus;

	public BusinessLogicException() {
		super();
	}

	public BusinessLogicException(String message, HttpStatus httpStatus) {
		super(message);
		this.httpStatus = httpStatus;
	}

	public BusinessLogicException(String message, Throwable cause) {
		super(message, cause);
	}

	public BusinessLogicException(String message) {
		super(message);
	}

	public BusinessLogicException(Throwable cause) {
		super(cause);
	}

}
