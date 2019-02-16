package com.example.tgiapi.exceptionhandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	private MessageSource messageSource;

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		String msgUser = this.messageSource.getMessage("mensagem.invalida", null, LocaleContextHolder.getLocale());
		String msgDev = ex.getCause().toString();
		List<Error> errors = Arrays.asList(new Error(msgUser, msgDev));
		return handleExceptionInternal(ex, errors, headers, HttpStatus.BAD_REQUEST, request);
	}


	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<Error> errors = createListErros(ex.getBindingResult());
		return handleExceptionInternal(ex, errors, headers, HttpStatus.BAD_REQUEST, request);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler({ EmptyResultDataAccessException.class })
	public ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex, WebRequest request) {
		String messageUser = messageSource.getMessage("recurso.nao-encontrado", null, LocaleContextHolder.getLocale());
		String messageDev = ex.toString();
		List<Error> errors = Arrays.asList(new Error(messageUser, messageDev));
		return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}

	/**
	 * 
	 * @param bindingResult
	 * @return List<Error>: Retorna uma lista com erros
	 */
	private List<Error> createListErros(BindingResult bindingResult) {
		List<Error> errors = new ArrayList<>();

		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			String msgUser = this.messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
			String msgDev = fieldError.toString();
			errors.add(new Error(msgUser, msgDev));
		}

		return errors;
	}

	public static class Error {

		private String msgUser;
		private String msgDev;

		public Error(String msgUser, String msgDev) {
			super();
			this.msgUser = msgUser;
			this.msgDev = msgDev;
		}

		public String getMsgUser() {
			return msgUser;
		}

		public String getMsgDev() {
			return msgDev;
		}

	}
}
