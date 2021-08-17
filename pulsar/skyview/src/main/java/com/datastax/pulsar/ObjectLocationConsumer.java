package com.datastax.pulsar;

import java.util.concurrent.TimeUnit;

import org.apache.pulsar.client.api.AuthenticationFactory;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.client.api.schema.GenericRecord;
import org.apache.pulsar.client.impl.schema.JSONSchema;

public class ObjectLocationConsumer {
    private static final String BROKER_SERVICE_URL = "pulsar+ssl://pulsar-aws-useast2.streaming.datastax.com:6651";
    private static final String  STREAM_NAME = "airport-events";
    private static final String NAMESPACE = "airport";
    private static final String TOPIC = "object-location";

    private PulsarClient client = null;
    
    public ObjectLocationConsumer() {
        try {
            // Create client object
            client = PulsarClient.builder()
            .serviceUrl(BROKER_SERVICE_URL)
            .authentication(
                AuthenticationFactory.token(Credentials.token)
            )
            .build();

            // Create consumer on a topic with a subscription
            Consumer<GenericRecord> consumer = client.newConsumer(Schema.AUTO_CONSUME())
            .topic("persistent://" + STREAM_NAME + "/" + NAMESPACE + "/" + TOPIC)
            .subscriptionName("object-listener")
            .subscribe();

            // Consumer time!
            System.out.println("Consumer: Looking for the message...");
            boolean finished = false;
            // Loop until a message is received
            do {
                // Block for up to 1 second for a message
                Message<GenericRecord> msg  = consumer.receive(1, TimeUnit.SECONDS);

                if(msg != null){
                    System.out.println("Message received!");
                    GenericRecord gRec = msg.getValue();
                    ObjectLocation recObj = new ObjectLocation(gRec);
                    System.out.println(recObj.toString());

                    // Acknowledge the message to remove it from the message backlog
                    System.out.println("Acknowledging message...");
                    consumer.acknowledge(msg);
                } else {
                    // No more messages at the moment
                    finished = true;
                }

            } while (!finished);
        } catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void main(String[] args) {
        ObjectLocationConsumer objConsumer = new ObjectLocationConsumer();
    }
}
