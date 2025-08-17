package com.rabbit.springboot.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value( "${rabbitmq.queue.name}")
    private String queue;

    @Value( "${rabbitmq.exchange.name}")
    private String exchange;

    public Queue queue() {
        return new Queue(queue);
    }

    public TopicExchange exchange() {
    	return new TopicExchange(exchange);
    }

}
