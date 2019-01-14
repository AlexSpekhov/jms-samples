package com.example.jms;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.ConnectionFactory;
import javax.jms.Session;
import java.util.Optional;

public class Launcher {

    public static void main(String[] args) {
        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:" + 50000);
        Launcher launcher = new Launcher();
        launcher.sendMessage(factory, "It's new message");
        String text = launcher.readMessage(factory);
        System.out.println(text);
    }

    public void sendMessage(ConnectionFactory factory, String text) {
        JmsProducer jmsProducer = new JmsProducer(factory);
        jmsProducer.sendMessageWithJms1_1("TEST_QUEUE", text);
    }

    public String readMessage(ConnectionFactory factory) {
        JmsConsumer consumer = new JmsConsumer(factory);
        Optional<String> message = consumer.readMessage("TEST_QUEUE", true, Session.SESSION_TRANSACTED);
        return message.orElse(null);
    }
}
