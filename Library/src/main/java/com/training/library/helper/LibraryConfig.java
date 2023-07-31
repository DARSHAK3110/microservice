package com.training.library.helper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.client.RestTemplate;

import com.training.library.interceptor.RestInterceptor;

import jakarta.servlet.http.HttpServletRequest;


@Configuration
public class LibraryConfig {

	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		  List<ClientHttpRequestInterceptor> interceptors
          = restTemplate.getInterceptors();
        if (CollectionUtils.isEmpty(interceptors)) {
            interceptors = new ArrayList<>();
        }
        interceptors.add(new RestInterceptor());
        restTemplate.setInterceptors(interceptors);
        return restTemplate;
    }
	
	@Bean
	public SmartValidator localValidatorFactoryBean() {
	   return new LocalValidatorFactoryBean();
	}

 }