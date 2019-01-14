package com.example.broker;

import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;

public class BrokerLauncher {
    public static void main(String[] args) {
        try {
            BrokerService brokerService = BrokerFactory.createBroker("broker:(tcp://localhost:61616)");
            brokerService.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
