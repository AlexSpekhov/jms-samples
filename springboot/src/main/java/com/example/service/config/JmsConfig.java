package com.example.service.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.jms.ConnectionFactory;
import java.util.Date;

@Configuration
@EnableScheduling
public class JmsConfig {

    CachingConnectionFactory factory;
    volatile int size = 5;

    final String queueName = "TEST_QUEUE";

//    @Bean("cacheConnectionFactory")
//    public CachingConnectionFactory getConnectionFactory(ConnectionFactory connectionFactory) {
//        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(connectionFactory);
//        cachingConnectionFactory.setSessionCacheSize(10);
//        return cachingConnectionFactory;
//    }

    @Bean
    public JmsTemplate getJmsTemplate() {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:" + 50000);
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(connectionFactory);
        cachingConnectionFactory.setSessionCacheSize(10);
        cachingConnectionFactory.setCacheProducers(false);
        factory = cachingConnectionFactory;
        JmsTemplate jmsTemplate = new JmsTemplate(cachingConnectionFactory);
        jmsTemplate.setDefaultDestinationName(queueName);
        return jmsTemplate;
    }

    @Scheduled(cron = "*/5 * * * * ?")
    public void schedule() {
        size = 5;
        factory.setSessionCacheSize(size);
        System.out.println(factory.hashCode());
//        if (size == 5) {
//            size = 2;
//        } else if(size == 2) {
//            size = 5;
//        }
        System.out.println(new Date(System.currentTimeMillis()));
    }

}
