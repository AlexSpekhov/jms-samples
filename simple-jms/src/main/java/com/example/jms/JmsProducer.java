package com.example.jms;


import lombok.extern.slf4j.Slf4j;

import javax.jms.*;

@Slf4j
public class JmsProducer {

    private ConnectionFactory connectionFactory;

    public JmsProducer(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    //TODO    activemq does not yet support JMS2
    public void senaMessageWithJms2_0(String queueName, String text) {
        try (JMSContext context = connectionFactory.createContext()) {
            JMSProducer producer = context.createProducer();
            Queue queue = context.createQueue(queueName);
            Message message = context.createTextMessage(text);
            producer.send(queue, message);
        } catch (JMSRuntimeException ex) {
            log.error("Sending failed",ex);
        }
    }

    public void sendMessageWithJms1_1(String queueName, String text){
        try {
            Connection connection;
            connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            try {
                Queue queue = session.createQueue(queueName);
                MessageProducer producer = session.createProducer(queue);
                Message msg = session.createTextMessage(text);
                producer.send(msg);
                log.info("Message sent");
                session.close();
            } finally {
                if (session != null) {
                    session.close();
                }
                connection.close();
            }
        } catch (JMSException ex) {
            log.error("Sending failed",ex);
        }

    }
}
