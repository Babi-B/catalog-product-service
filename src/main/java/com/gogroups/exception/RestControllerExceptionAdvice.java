package com.gogroups.exception;

import java.util.Date;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.gogroups.payload.response.ErrorMessage;

@ControllerAdvice
public class RestControllerExceptionAdvice {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorMessage> globalExceptionHandler(Exception ex, WebRequest req) {
		ErrorMessage msge = new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), new Date(), ex.getMessage(),
				req.getDescription(false));

		return new ResponseEntity<ErrorMessage>(msge, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<ErrorMessage> handleMissingParams(MissingServletRequestParameterException ex,
			WebRequest req) {
		ErrorMessage msge = new ErrorMessage(HttpStatus.NOT_FOUND.value(), new Date(), ex.getMessage(),
				req.getDescription(false));

		return new ResponseEntity<ErrorMessage>(msge, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<ErrorMessage> noHandlerException(NoHandlerFoundException ex, WebRequest req) {
		ErrorMessage msge = new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), new Date(), ex.getMessage(),
				req.getDescription(false));

		return new ResponseEntity<ErrorMessage>(msge, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(AccountNotFoundException.class)
	public ResponseEntity<ErrorMessage> noHandlerException(AccountNotFoundException ex, WebRequest req) {
		ErrorMessage msge = new ErrorMessage(HttpStatus.NOT_FOUND.value(), new Date(), ex.getMessage(),
				req.getDescription(false));

		return new ResponseEntity<ErrorMessage>(msge, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(TokenRefreshException.class)
	public ResponseEntity<ErrorMessage> handleTokenRefreshException(TokenRefreshException ex, WebRequest request) {
		ErrorMessage msge =new ErrorMessage(HttpStatus.FORBIDDEN.value(), new Date(), ex.getMessage(),
				request.getDescription(false));
		
		return new ResponseEntity<ErrorMessage>(msge, HttpStatus.FORBIDDEN);
	}

}
