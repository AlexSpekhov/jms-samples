package com.example.jms;

import lombok.extern.slf4j.Slf4j;

import javax.jms.*;
import java.util.Optional;

@Slf4j
public class JmsConsumer {

    private ConnectionFactory connectionFactory;

    public JmsConsumer(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public Optional<String> readMessage(String queueName, boolean transacted, int acknowledgeMode) {
        try {
            Connection connection;
            connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(transacted,acknowledgeMode);
            try {
                Queue queue = session.createQueue(queueName);
                MessageConsumer consumer = session.createConsumer(queue);
                log.info("Wait message...");
                TextMessage message = (TextMessage) consumer.receive();
//                session.commit();
//                session.rollback();
//                message.acknowledge();
                log.info("Message received");
                return Optional.ofNullable(message.getText());
            } finally {
//                if (session != null) {
//                    session.close();
//                }
//                connection.close();
            }
        } catch (JMSException ex) {
            log.error("Reading failed", ex);
            return Optional.empty();
        }
    }
}
