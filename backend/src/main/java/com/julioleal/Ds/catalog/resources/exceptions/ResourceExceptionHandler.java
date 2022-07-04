package com.julioleal.Ds.catalog.resources.exceptions;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.julioleal.Ds.catalog.services.exceptions.DataBaseException;
import com.julioleal.Ds.catalog.services.exceptions.ResourceNotFoundException;

@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<StandartError> entityNotFound(ResourceNotFoundException e, HttpServletRequest request) {
		StandartError err = new StandartError();
		HttpStatus status = HttpStatus.NOT_FOUND;
		err.setTimestamp(Instant.now());
		err.setStatus(status.value());
		err.setError("Resource not found");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(DataBaseException.class)
	public ResponseEntity<StandartError> database(DataBaseException e, HttpServletRequest request) {
		StandartError err = new StandartError();
		HttpStatus status = HttpStatus.BAD_REQUEST;
		err.setTimestamp(Instant.now());
		err.setStatus(status.value());
		err.setError("Database exception");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());
		return ResponseEntity.status(status).body(err);

	}
}
