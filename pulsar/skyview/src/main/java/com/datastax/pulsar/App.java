package com.datastax.pulsar;

import java.util.Timer;
import org.apache.pulsar.client.api.AuthenticationFactory;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.impl.schema.JSONSchema;

public class App {
    public static void main(String[] args)
    {
        final String BROKER_SERVICE_URL = "pulsar+ssl://pulsar-aws-uswest2.streaming.datastax.com:6651";
        final String STREAM = "airport-events";
        final String NAMESPACE = "airport";
        final String TOPIC = "object-location";

        PulsarClient client = null;
        Producer<ObjectLocation> producer = null;

        try {
            // Create client object
            client = PulsarClient.builder()
            .serviceUrl(BROKER_SERVICE_URL)
            .authentication(
                AuthenticationFactory.token(Credentials.token)
            )
            .build();

            // Create producer on a topic
            producer = client.newProducer(JSONSchema.of(ObjectLocation.class))
            .topic("persistent://" + STREAM + "/" + NAMESPACE + "/" + TOPIC)
            .create();

            // Create a producer for the test_table
            Producer<Test_Table> producerTest = client.newProducer(JSONSchema.of(Test_Table.class))
            .topic("persistent://" + STREAM + "/" + NAMESPACE + "/" + "test-table")
            .create();

            Timer timer = new Timer();
            Flight flight1 = new Flight(producer, "ABS2045", "Boeing 737", 1);

            Test test = new Test(producerTest, "ABS2045");
            // Update the position every second
            timer.schedule(flight1, 0, 1000);
            timer.schedule(test, 0, 1000);
        } catch(PulsarClientException pcex) {
            pcex.printStackTrace();
        }
    }
}