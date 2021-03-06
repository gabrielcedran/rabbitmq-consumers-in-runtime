package br.com.cedran.rabbit.config.rabbitmq;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.cedran.rabbit.gateway.rabbitmq.ImportantMessageListener;
import br.com.cedran.rabbit.model.ImportantMessage;

@Configuration
public class ConsumerConfig {

    @Value(value = "${concurrentConsumers:1}")
    private Integer concurrentConsumers;

    @Value(value = "${maxConcurrentConsumers:1}")
    private Integer maxConcurrentConsumers;

    @Bean
    SimpleMessageListenerContainer simpleMessageListenerContainer(final ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer listenerFactory = new SimpleMessageListenerContainer();
        listenerFactory.setConnectionFactory(connectionFactory);
        listenerFactory.setConcurrentConsumers(concurrentConsumers);
        listenerFactory.setMaxConcurrentConsumers(maxConcurrentConsumers);
        listenerFactory.setMessageListener(listenerAdapter);
        listenerFactory.setQueueNames("importantMessageQueueSimpleMessage");
        return listenerFactory;
    }

    @Bean
    Jackson2JsonMessageConverter jacksonConverter() {
        Jackson2JsonMessageConverter jacksonConverter = new Jackson2JsonMessageConverterCustom(ImportantMessage.class);
        return jacksonConverter;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(ImportantMessageListener importantMessageListener) {
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(importantMessageListener, "execute1");
        messageListenerAdapter.setMessageConverter(jacksonConverter());
        return messageListenerAdapter;
    }

    @Bean
    SimpleRabbitListenerContainerFactory simpleRabbitListenerContainer(final ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        SimpleRabbitListenerContainerFactory listenerFactory = new SimpleRabbitListenerContainerFactory();
        listenerFactory.setConnectionFactory(connectionFactory);
        listenerFactory.setConcurrentConsumers(concurrentConsumers);
        listenerFactory.setMaxConcurrentConsumers(maxConcurrentConsumers);
        return listenerFactory;
    }
}
