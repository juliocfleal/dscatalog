package com.julioleal.Ds.catalog.resources.exceptions;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.julioleal.Ds.catalog.services.exceptions.EntityNotfindException;

@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler(EntityNotfindException.class)
	public ResponseEntity<StandartError> entityNotFound(EntityNotfindException e, HttpServletRequest request) {
		StandartError err = new StandartError();
		err.setTimestamp(Instant.now());
		err.setStatus(HttpStatus.NOT_FOUND.value());
		err.setError("Resource not found");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
	}

}
