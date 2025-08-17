package com.rabbit.springboot.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value( "${rabbitmq.queue.name}")
    private String queue;

    @Value( "${rabbitmq.exchange.name}")
    private String exchange;

    @Value( "${rabbitmq.routing-key.name}")
    private String routing_key;

    @Bean
    public Queue queue() {
        return new Queue(queue);
    }

    @Bean
    public TopicExchange exchange() {
    	return new TopicExchange(exchange);
    }

    // Bind the queue to the exchange with the routing key.
    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routing_key);
    }

    // ConnectionFactory
    // RabbitTemplate
    // RabbitAdmin


}
