package com.training.library.services;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.training.library.dto.response.CustomBaseResponseDto;
import com.training.library.dto.response.MessageResponseDto;

import jakarta.validation.Valid;

@Service
@PropertySource("classpath:message.properties")
public class AutoService {
	@Value("${rabbitmq.queue.name}")
	private String queue;
	@Value("${rabbitmq.exchange.name}")
	private String exchange;
	@Value("${rabbitmq.routingkey.name}")
	private String routingkey;
	@Autowired
	private RabbitTemplate rabbitTemplate;
	private static final String OPERATION_SUCCESS = "operation.success";
	@Autowired
	private Environment env;
	public ResponseEntity<CustomBaseResponseDto> autoDeleteMessage(@Valid Object obj, String userName,String entity) {
		MessageResponseDto dto = new MessageResponseDto(); 
		dto.setEntity(entity);
		dto.setEntityRequestDto(obj);
		dto.setUserName(userName);
		rabbitTemplate.convertAndSend(exchange, routingkey,dto);
		
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}

}
