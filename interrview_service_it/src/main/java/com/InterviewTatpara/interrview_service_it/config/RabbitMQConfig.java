package com.InterviewTatpara.interrview_service_it.config;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE = "video_analysis_queue";
    public static final String EXCHANGE = "interview_exchange";
    public static final String ROUTING_KEY = "video_routing_key";

    @Bean
    public Queue queue() { return new Queue(QUEUE); }

    @Bean
    public TopicExchange exchange() { return new TopicExchange(EXCHANGE); }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    // --- PRODUCER VERSION (Standard) ---
    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }
    // -----------------------------------

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}