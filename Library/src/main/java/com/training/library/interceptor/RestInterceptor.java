package com.training.library.interceptor;

import java.io.IOException;

import org.mapstruct.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.servlet.http.HttpServletRequest;

public class RestInterceptor implements ClientHttpRequestInterceptor{


	private HttpServletRequest requestParent;
	


	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		String token = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
		request.getHeaders().add("Authorization", token);
		 return execution.execute(request, body);
	}

}
