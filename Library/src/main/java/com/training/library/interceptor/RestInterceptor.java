package com.training.library.interceptor;

import java.io.IOException;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.context.SecurityContextHolder;

public class RestInterceptor implements ClientHttpRequestInterceptor{



	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		String token = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
		request.getHeaders().add("Authorization", token);
		 return execution.execute(request, body);
	}

}
