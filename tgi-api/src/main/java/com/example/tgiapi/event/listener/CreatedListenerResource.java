package com.example.tgiapi.event.listener;

import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.tgiapi.event.CreatedEventResource;

@Component
public class CreatedListenerResource implements ApplicationListener<CreatedEventResource> {

	@Override
	public void onApplicationEvent(CreatedEventResource createdEventResource) {
		HttpServletResponse response = createdEventResource.getResponse();
		Long cod = createdEventResource.getCod();
		
		addHeaderLocation(response, cod);
	}

	private void addHeaderLocation(HttpServletResponse response, Long cod) {
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{categoryId}")
				.buildAndExpand(cod).toUri();
			response.setHeader("Location", uri.toASCIIString());
	}

}
