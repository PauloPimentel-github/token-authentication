package com.example.tgiapi.event;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationEvent;

public class CreatedEventResource extends ApplicationEvent {

	private static final long serialVersionUID = 1L;
	
	private HttpServletResponse response;
	private Long cod;

	public CreatedEventResource(Object source, HttpServletResponse response, Long cod) {
		super(source);
		this.response = response;
		this.cod = cod;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public Long getCod() {
		return cod;
	}

}
