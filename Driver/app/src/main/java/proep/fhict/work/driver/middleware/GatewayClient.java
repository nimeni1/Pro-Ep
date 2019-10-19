package proep.fhict.work.driver.middleware;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class GatewayClient {

    private static final String RECEIVE_Q = "middlewareToDriver";
    private static final String SEND_Q = "driverToMiddleware";
    private static final String IP = "192.168.43.142";
    private static Channel channel;

    public static void sendMessage(final String body) {
        final ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("user");
        factory.setPassword("user");
        factory.setHost(IP);
        new Thread(new Runnable() {
            @Override
            public void run() {
            Connection connection;
                try {
                    connection = factory.newConnection();
                    channel = connection.createChannel();
                    channel.queueDeclare(SEND_Q,true,false,false,null);
                    channel.basicPublish("", SEND_Q, null, body.getBytes());
                } catch (IOException | TimeoutException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void receiveMessage( final DefaultConsumer defaultConsumer) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ConnectionFactory factory = new ConnectionFactory();
                factory.setUsername("user");
                factory.setPassword("user");
                factory.setHost(IP);
                try {
                    Connection connection = factory.newConnection();
                    channel = connection.createChannel();
                    channel.queueDeclare(RECEIVE_Q, true, false, false, null);
                    channel.basicConsume(RECEIVE_Q,true,defaultConsumer);
                } catch (TimeoutException | IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public Channel getChannel() {
        return channel;
    }
}
