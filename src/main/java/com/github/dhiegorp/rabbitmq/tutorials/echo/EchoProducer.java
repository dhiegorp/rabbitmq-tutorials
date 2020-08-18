package com.github.dhiegorp.rabbitmq.tutorials.echo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Properties;

/**
 *
 * Producer part of our example
 *
 *   P ---> [][][][][]
 *            queue
 *
 * Based on a rabbitmq`s tutorial. For more information follow the link:
 * https://github.com/rabbitmq/rabbitmq-tutorials/blob/master/java/Send.java
 *
 */
public class EchoProducer {

    private String hostAddress;
    private String queue;
    private String exchange;
    private Connection connection;
    private Channel channel;

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

    public EchoProducer() {
    }

    public EchoProducer(Properties props) {
        setup(props);
    }

    public void createQueue() throws Exception {
        System.out.println("\tcreating queue...");
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(this.hostAddress);

        connection = factory.newConnection();
        channel = connection.createChannel();


        /**
         * channel.queueDeclare receives the
         * queue name,
         * durable,
         * exclusive
         * autoDelete,
         * argument map
         */
        channel.queueDeclare(this.queue, false, false, false, null);
    }

    public void sendMessage(String message) throws Exception {
        channel.basicPublish(this.exchange, this.queue, null, message.getBytes());
        System.out.println("[EchoProducer] Sent : '" + message + "'");
    }


    public void releaseResources() {
        try {
            this.channel.close();
            this.connection.close();
            System.out.println("[EchoProducer] resources cleaned.");
        } catch(Exception e) {
            System.out.println("[EchoProducer] error while cleaning up resources! \n" + e);
        }
    }

}
