package br.com.hopsoftware.rabbitmq.tutorial.echoserver;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.util.Properties;

/**
 *
 *  Consumer part of our example.
 *
 *  [][][][] ---> C
 *   queue
 *
 *  Based on a rabbitmq`s tutorial. For more information follow the link:
 *  https://github.com/rabbitmq/rabbitmq-tutorials/blob/master/java/Recv.java
 */
public class EchoConsumer extends Thread {

    private String hostAddress;
    private String exchange;
    private String queue;
    private Connection connection;
    private Channel channel;

    public EchoConsumer() {
    }

    public EchoConsumer(Properties props) {
        setup(props);
    }

    protected void setup(String hostAddress, String exchange, String queue) {
        this.hostAddress = hostAddress;
        this.exchange = exchange;
        this.queue = queue;
    }

    protected void setup(Properties props) {
        this.setup(
                props.getProperty("host"),
                props.getProperty("exchange"),
                props.getProperty("queue")
        );
    }

    public void listenQueue() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(this.hostAddress);
        connection = factory.newConnection();
        channel  = connection.createChannel();

        channel.queueDeclare(this.queue, false, false, false, null);

        System.out.println("[EchoConsumer] Listening  messages from queue '" + queue + "'" );

        DeliverCallback callback = (consumerTag, delivery) -> {
            String receivedMessage = new String(delivery.getBody(), "UTF-8");
            System.out.println("[EchoConsumer] Received : '" + receivedMessage + "'\n");
        };

        channel.basicConsume(this.queue, true, callback, consumerTag -> {});

    }


    @Override
    public synchronized void start() {
        System.out.println("[EchoConsumer] Initializing Consumer... ");
        try {
            this.listenQueue();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void releaseResources() {
        try {
            this.channel.close();
            this.connection.close();
            System.out.println("[EchoConsumer] resources cleaned.");
        } catch(Exception e) {
            System.out.println("[EchoConsumer] error while cleaning up resources! \n" + e);
        }
    }


}
