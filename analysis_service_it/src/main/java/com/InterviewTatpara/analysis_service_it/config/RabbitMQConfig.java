package com.InterviewTatpara.analysis_service_it.config;

import com.InterviewTatpara.analysis_service_it.dto.VideoMessage; // <--- Import your local DTO
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

import java.util.HashMap;
import java.util.Map;

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

    @Bean
    public MessageConverter converter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        
        DefaultClassMapper classMapper = new DefaultClassMapper();
        
        // 1. Trust all packages
        classMapper.setTrustedPackages("*");

        // 2. THE FIX: Map the Producer's class name to the Consumer's class
        Map<String, Class<?>> idClassMapping = new HashMap<>();
        // Copy the exact class name from your error log
        idClassMapping.put("com.InterviewTatpara.interrview_service_it.dto.VideoMessage", VideoMessage.class);
        
        classMapper.setIdClassMapping(idClassMapping);
        
        converter.setClassMapper(classMapper);
        return converter;
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}