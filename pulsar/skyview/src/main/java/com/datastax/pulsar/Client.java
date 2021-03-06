package com.datastax.pulsar;

import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.apache.pulsar.client.api.AuthenticationFactory;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.client.api.schema.GenericRecord;

/**
 * This is a client to the object-location pulsar topic
 */
public class Client extends TimerTask {
    final String SERVICE_URL = "pulsar+ssl://pulsar-aws-useast2.streaming.datastax.com:6651";
    final String TENANT_NAME = "airport-events";
    final String NAMESPACE = "airport";
    final String TOPIC = "object-location";
    private PulsarClient client = null;
    private Consumer<GenericRecord> consumer = null;
    
    public Client() {
        try {
            // Create client object
            client = PulsarClient.builder()
                .serviceUrl(SERVICE_URL)
                .authentication(
                    AuthenticationFactory.token(Credentials.token)
                )
                .build();

            // Create consumer on a topic with a subscription
            consumer = client.newConsumer(Schema.AUTO_CONSUME())
                .topic("persistent://" + TENANT_NAME + "/" + NAMESPACE + "/" + TOPIC)
                .subscriptionName("object-listener")
                .subscribe();
        } catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void run() {
        try {
            System.out.println("Consumer: Looking for the message...");

            // Block for up to 1 second for a message
            Message<GenericRecord> msg  = consumer.receive(1, TimeUnit.SECONDS);

            while(msg != null) {
                System.out.println("Message received!");
                GenericRecord gRec = msg.getValue();
                ObjectLocation recObj = new ObjectLocation(gRec);
                System.out.println(recObj.toString());

                // Acknowledge the message to remove it from the message backlog
                System.out.println("Acknowledging message...");
                consumer.acknowledge(msg);

                // Block for up to 1 second for a message
                msg  = consumer.receive(1, TimeUnit.SECONDS);
            }
        } catch(PulsarClientException pcex) {
            pcex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client objConsumer = new Client();

        objConsumer.run();
        // Timer timer = new Timer();
        // Update the position every second
        // timer.schedule(objConsumer, 0, 1000);
    }
}
