package com.homework.mikeyProject.handler;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.homework.mikeyProject.exceptions.BusinessLogicException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> serverExceptionHandler(Exception exception) {
		log.info("serverExceptionHandler Start");
		log.error(exception.getMessage(), exception);
		DefaultErrorMessage errorMessage = new DefaultErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR,
				exception.getLocalizedMessage(), "Error Exception");
		log.info("serverExceptionHandler End");
		return new ResponseEntity<>(errorMessage, new HttpHeaders(), errorMessage.getStatus());
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<DefaultErrorMessage> httpRequestMethodNotSupportedException(
			HttpRequestMethodNotSupportedException exception) {
		String error = exception.getMessage();
		DefaultErrorMessage errorMessage = new DefaultErrorMessage(HttpStatus.METHOD_NOT_ALLOWED,
				exception.getLocalizedMessage(), error);
		return new ResponseEntity<>(errorMessage, new HttpHeaders(), errorMessage.getStatus());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<DefaultErrorMessage> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
		List<String> errors = exception.getBindingResult().getFieldErrors().stream()
				.map(fieldError -> fieldError.getDefaultMessage()).collect(Collectors.toList());
		DefaultErrorMessage errorMessage = new DefaultErrorMessage(HttpStatus.BAD_REQUEST,
				exception.getLocalizedMessage(), errors);
		return new ResponseEntity<>(errorMessage, new HttpHeaders(), errorMessage.getStatus());
	}

	@ExceptionHandler(BusinessLogicException.class)
	public ResponseEntity<DefaultErrorMessage> handleBusinessLogicException(BusinessLogicException exception) {
		String error = exception.getMessage();
		log.info("BusinessLogicException error : " + error);
		DefaultErrorMessage errorMessage = new DefaultErrorMessage(exception.getHttpStatus(),
				exception.getLocalizedMessage(), error);
		return new ResponseEntity<>(errorMessage, new HttpHeaders(), errorMessage.getStatus());
	}

}
