package com.training.library.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.amqp.support.AmqpHeaders;

import com.rabbitmq.client.Channel;
import com.training.library.dto.response.CustomBaseResponseDto;
import com.training.library.dto.response.MessageResponseDto;
import com.training.library.mapper.DtoToEntityImpl;

@Service
public class RabbitMQListner {
	
	@Autowired
	private RestTemplate template;
	@Autowired
	private RabbitTemplate rabbitTemplate;
	//@RabbitListener(queues = {"library_queue"})
	public void consumer(Message message, Channel channel, MessageResponseDto result, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
	System.out.println(result.getEntity());
		if(result.getEntity().equals("location")) {
		template.patchForObject("http://library/api/v1/locations/auto/"+result.getEntityId(),result.getEntityRequestDto(),ResponseEntity.class);
	}
	}
}
