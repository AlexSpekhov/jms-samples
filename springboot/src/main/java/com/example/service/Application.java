package com.example.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.JmsException;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);


        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
        ExecutorService executorService = Executors.newCachedThreadPool();
        IntStream.range(0, 100).forEach(value -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            CachingConnectionFactory connectionFactory = (CachingConnectionFactory) jmsTemplate.getConnectionFactory();
            System.out.println(connectionFactory.getSessionCacheSize());
            executorService.submit(() -> jmsTemplate.convertAndSend(Thread.currentThread().getName()));
            executorService.submit(() -> {
            try {
                Object o = jmsTemplate.receiveAndConvert();
            } catch (JmsException ex) {
                    System.out.println(ex);
            }
            });
        });


//        JmsTemplate jmsTemplate
        System.out.println(context);
    }
}
