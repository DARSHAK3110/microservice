package com.training.library.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Configuration
@PropertySource("classpath:message.properties")
	public class RabbitMQConfig {

		@Value("${rabbitmq.queue.name}")
		private String queue;
		@Value("${rabbitmq.exchange.name}")
		private String exchange;
		@Value("${rabbitmq.routingkey.name}")
		private String routingkey;
		@Bean
		public Queue queue() {
			return new Queue(queue);
		}
		
		@Bean
		public DirectExchange exchange() {
			return new DirectExchange(exchange);
		}
		
		@Bean
		public Binding binding() {
			return BindingBuilder.bind(queue()).to(exchange()).with(routingkey);
		}
	    @Bean
	    public MessageConverter converter(){
	        return new Jackson2JsonMessageConverter();
	    }
	    @Bean
	    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
	        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
	        rabbitTemplate.setMessageConverter(converter());
	        return rabbitTemplate;
	    }
}
