package br.com.hopsoftware.rabbitmq.tutorial.echoserver;

import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

/**
 *
 * This example was created based on a rabbitmq`s tutorial.
 * In our case, we`ve tried to create an Echo Server, obtaining messages from the user`s keyboard and
 * send them to a queue that will be consumed - printing the message sent.
 * Messages are sent and comsumed asynchonously.
 * This example shows the use of rabbimq`s java client for the
 * simplest scenario: a Producer {@link EchoProducer} sends a direct message to a queue
 * (default exchange), to be consumed by a Consumer {@link EchoConsumer}
 *
 *  P ---> [][][][][] ---> C
 *           queue
 *
 * https://www.rabbitmq.com/tutorials/tutorial-one-java.html
 */
public class EchoMessageTutorial {


    private static final String CONFIG_FILE = "echo.properties";



    public static void main(String[] args) {
        System.out.println("Starting 'ECHO SERVER'... ");

        Properties config = readConfiguration();

        EchoConsumer consumer = new EchoConsumer(config);
        EchoProducer producer = new EchoProducer(config);

        try {

            producer.createQueue();
            consumer.start();

            try(Scanner scanner = new Scanner(System.in)) {
                System.out.println("To send messages type whatever you want and press return.");
                System.out.print(">>  ");
                while(scanner.hasNextLine()) {
                    producer.sendMessage(scanner.nextLine());
                    Thread.sleep(250); //just trying to avoid a console print mess
                    System.out.print(">> ");
                }
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    public static Properties readConfiguration() {
        System.out.println("\treading configuration...");
        Properties properties = new Properties();
        try(InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIG_FILE)) {
            properties.load(is);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static void bindShutdownHook(EchoConsumer consumer, EchoProducer producer) {
        System.out.println("\tbinding shutdown hook...");
        Thread hook = new Thread(() -> {
            System.out.println("[Echo] Shutdown hook detected! Cleaning/Closing resources...");
            consumer.releaseResources();
            producer.releaseResources();
        });
        Runtime.getRuntime().addShutdownHook(hook);
    }


}
