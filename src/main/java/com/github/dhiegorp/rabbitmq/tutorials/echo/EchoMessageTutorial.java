package com.github.dhiegorp.rabbitmq.tutorials.echo;

import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

/**
 *
 * This example was created based on a rabbitmq`s tutorial.
 * In our case, we`ve tried to create an Echo Server, obtaining messages from the user`s keyboard and
 * send them to a queue that will be consumed - printing the message sent.
 * Messages are sent and comsumed asynchonously, so there are no guarantees that the console will show
 * the messages sent and received in a sequential way.
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


    /**
     * Initiate the queue and start both producer and consumer to be used.
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("Starting 'ECHO SERVER'... ");

        Properties config = readConfiguration();

        EchoConsumer consumer = new EchoConsumer(config);
        EchoProducer producer = new EchoProducer(config);

        try {

            producer.createQueue();
            consumer.start();
            openCMD(producer);

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * for test purposes
     * @param producer
     * @throws Exception
     */
    public static void openCMD(EchoProducer producer) throws Exception{
        try(Scanner scanner = new Scanner(System.in)) {
            System.out.println("To send messages, type whatever you want and press return. To finish this program, type 'bye!'");
            System.out.print(">>  ");
            String line = null;
            while(scanner.hasNextLine()) {
                line = scanner.nextLine();
                producer.sendMessage(line);
                Thread.sleep(250); //just trying to avoid a console print mess
                if("bye!".equalsIgnoreCase(line.trim())) {
                    System.out.println("Bye!");
                    System.exit(0);
                } else {
                    System.out.print(">> ");
                }
            }
        }

    }

    /**
     * load config properties
     * @return
     */
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

    /**
     * Set a shutdown hook to release resources
     * @param consumer
     * @param producer
     */
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
