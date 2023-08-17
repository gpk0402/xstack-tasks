package com.epam.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
@RestControllerAdvice
@Slf4j
public class MyExceptionHandler {
	@ExceptionHandler(InvalidAccessException.class)
	@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
	public ExceptionResponse handleInvalidAccessException(InvalidAccessException e, WebRequest w)
	{
		log.info("UserException Occurred : {}",e);
		return new ExceptionResponse(new Date().toString(),e.getMessage(),HttpStatus.NOT_FOUND.toString(),w.getDescription(false));
	}
	
	@ExceptionHandler(RuntimeException.class)
	@ResponseStatus(code=HttpStatus.INTERNAL_SERVER_ERROR)
	public ExceptionResponse handleRuntimeException(RuntimeException e,WebRequest w)
	{
		log.info("RunTimeException Occured : {}",e);
		return new ExceptionResponse(new Date().toString(),e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.toString(),w.getDescription(false));
	}

}
