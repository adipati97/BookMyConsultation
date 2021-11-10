package com.upgrad.bookmyconsultation.handler;


import com.upgrad.bookmyconsultation.controller.ext.ErrorResponse;
import com.upgrad.bookmyconsultation.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(AuthenticationFailedException.class)
	public final ResponseEntity<ErrorResponse> handleAuthenticationFailedException(AuthenticationFailedException ex, WebRequest request) {
		return new ResponseEntity(errorResponse(ex), UNAUTHORIZED);
	}

	@ExceptionHandler(UnauthorizedException.class)
	public final ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException ex, WebRequest request) {
		return new ResponseEntity(errorResponse(ex), UNAUTHORIZED);
	}

	@ExceptionHandler(AuthorizationFailedException.class)
	public final ResponseEntity<ErrorResponse> handleAuthorizationFailedException(AuthorizationFailedException ex, WebRequest request) {
		return new ResponseEntity(errorResponse(ex), FORBIDDEN);
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public final ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
		return new ResponseEntity(errorResponse(ex), NOT_FOUND);
	}

	@ExceptionHandler(ApplicationException.class)
	public final ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException ex, WebRequest request) {
		return new ResponseEntity(errorResponse(ex), UNPROCESSABLE_ENTITY);
	}

	@ExceptionHandler(RestException.class)
	public final ResponseEntity<ErrorResponse> handleRestException(RestException ex, WebRequest request) {
		return new ResponseEntity(errorResponse(ex), UNPROCESSABLE_ENTITY);
	}

	@ExceptionHandler(RuntimeException.class)
	public final ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex, WebRequest request) {
		return new ResponseEntity(errorResponse(ex), INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(InvalidInputException.class)
	public ResponseEntity<ErrorResponse> handleInvalidInput(InvalidInputException e) {
		return new ResponseEntity(errorResponse(e), HttpStatus.BAD_REQUEST);
	}

	//mark as ExceptionHandler for the class SlotUnavailableException
	//create a method handleSlotUnavailableException with return type of ResponseEntity
		//return http response for bad request with error code and a message
	@ExceptionHandler(SlotUnavailableException.class)
	public ResponseEntity handleSlotUnavailableException() {
		return ResponseEntity
			.badRequest()
			.body(new ErrorResponse().code("ERR_SLOT_UNAVAILABLE").message("Either the slot is already booked or not available"));
	}


	private ErrorResponse errorResponse(final ApplicationException exc) {
		exc.printStackTrace();
		return new ErrorResponse().code(exc.getErrorCode().getCode()).message(exc.getMessage());
	}

	private ErrorResponse errorResponse(final RestException exc) {
		exc.printStackTrace();
		return new ErrorResponse().code(exc.getErrorCode().getCode()).message(exc.getMessage());
	}

	private ErrorResponse errorResponse(final RuntimeException exc) {
		exc.printStackTrace();

		final StringWriter stringWriter = new StringWriter();
		exc.printStackTrace(new PrintWriter(stringWriter));

		String message = exc.getMessage();
		if (message == null) {
			message = GenericErrorCode.GEN_001.getDefaultMessage();
		}
		return new ErrorResponse().code(GenericErrorCode.GEN_001.getCode()).message(message).rootCause(stringWriter.getBuffer().toString());
	}

	/**
	 * Return a list of attribute that have validation errors as the error response instead of the entire stack trace.
	 */
	private ErrorResponse errorResponse(final InvalidInputException invalidInputException) {
		invalidInputException.printStackTrace();

		final StringWriter stringWriter = new StringWriter();
		invalidInputException.printStackTrace(new PrintWriter(stringWriter));

		String message = invalidInputException.getMessage();
		if (message == null) {
			message = InvalidInputErrorCode.INVALID_INPUT.getDefaultMessage();
		}
		return new ErrorResponse()
			.code(InvalidInputErrorCode.INVALID_INPUT.getCode())
			.message(message)
			.invalidInputs(invalidInputException.getAttributeNames());
	}

	private ErrorResponse errorResponse(final UnauthorizedException exception) {
		exception.printStackTrace();
		return new ErrorResponse()
			.code(exception.getErrorCode().getCode())
			.message(exception.getMessage());
	}

	private ErrorResponse errorResponse(final AuthorizationFailedException exception) {
		exception.printStackTrace();
		return new ErrorResponse()
			.code(exception.getErrorCode().getCode())
			.message(exception.getMessage());
	}

	private ErrorResponse errorResponse(final AuthenticationFailedException exception) {
		exception.printStackTrace();
		return new ErrorResponse()
			.code(exception.getErrorCode().getCode())
			.message(exception.getMessage());
	}

}