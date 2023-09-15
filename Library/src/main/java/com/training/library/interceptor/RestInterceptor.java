package com.training.library.interceptor;

import java.io.IOException;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.context.SecurityContextHolder;

public class RestInterceptor implements ClientHttpRequestInterceptor{


	private String UNIVERSAL_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJuYW1lIjoiZGFyc2hhayB6YWRhZml5YSIsInN1YiI6Ijk3MjU5NTMwMzUiLCJpYXQiOjE2OTMyODIwMzUsImV4cCI6MTg1MTA2NjY2NX0.lDUfnvWFhRS4Tdvc8k7sur-PTbpXlq7YsbcrS0bNbvQ" ;

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		request.getHeaders().add("Authorization", UNIVERSAL_TOKEN);
		 return execution.execute(request, body);
	}

}
