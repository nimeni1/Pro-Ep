package com.fhict.proep.client.middleware;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class GatewayClient {

    private static final String RECEIVE_Q = "middlewareToClient";
    private static final String SEND_Q = "clientToMiddleware";
    private static final String IP = "192.168.43.142";
    private static Channel channel;

    public static void sendMessage(final String body) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("user");
        factory.setPassword("user");
        factory.setHost(IP);
        new Thread(() -> {
            Connection connection;
            try {
                connection = factory.newConnection();
                channel = connection.createChannel();
                channel.queueDeclare(SEND_Q,true,false,false,null);
                channel.basicPublish("", SEND_Q, null, body.getBytes());
            } catch (IOException | TimeoutException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void receiveMessage(DefaultConsumer defaultConsumer) {
        new Thread(() -> {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setUsername("user");
            factory.setPassword("user");
            factory.setHost(IP);
            try {
                Connection connection = factory.newConnection();
                channel = connection.createChannel();
                channel.queueDeclare(RECEIVE_Q, true, false, false, null);
                channel.basicConsume(RECEIVE_Q,false,defaultConsumer);
            } catch (TimeoutException | IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static Channel getChannel() {
        return channel;
    }
}
