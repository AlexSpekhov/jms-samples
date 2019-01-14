import com.example.jms.JmsConsumer;
import com.example.jms.JmsProducer;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.jms.*;
import javax.print.attribute.IntegerSyntax;
import java.util.Enumeration;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class MultithreadingTest {

    ConnectionFactory connectionFactory;

    QueueBrowser browser;

    final String queueName = "TEST_QUEUE";

    @BeforeClass
    public void setUp() {
        connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:" + 50000);
        Connection connection = null;
        try {
            connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue(queueName);
            browser = session.createBrowser(queue);
        } catch (JMSException e) {
            e.printStackTrace();
        }


    }


    @Test
    public void testSendAndReceive(){
        JmsProducer jmsProducer = new JmsProducer(connectionFactory);
        JmsConsumer consumer = new JmsConsumer(connectionFactory);
        ExecutorService executorService = Executors.newCachedThreadPool();
        IntStream.range(0,100).forEach(value -> {
            executorService.submit(() -> jmsProducer.sendMessageWithJms1_1(queueName, "Test"));
            executorService.submit(() -> receiveMessage(consumer));
            executorService.submit(() -> System.out.println(getSizeQueue()));
        });
        try {
            Thread.sleep(200000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int getSizeQueue() {
        int size = 0;
        try {
            Enumeration enumeration = browser.getEnumeration();
            while (enumeration.hasMoreElements()) {
                enumeration.nextElement();
                size++;
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return size;
    }

    private void receiveMessage(JmsConsumer consumer) {
        Optional<String> s = consumer.readMessage(queueName, true, Session.AUTO_ACKNOWLEDGE);
        s.ifPresent(System.out::println);
    }
}
