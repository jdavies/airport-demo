package com.datastax.pulsar;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.pulsar.client.api.AuthenticationFactory;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.client.api.schema.GenericRecord;
import org.apache.pulsar.client.impl.schema.JSONSchema;

public class App extends TimerTask {

    private static final String BROKER_SERVICE_URL = "pulsar+ssl://pulsar-aws-useast2.streaming.datastax.com:6651";
    private static final String  STREAM_NAME = "airport-events2";
    private static final String NAMESPACE = "default";
    private static final String TOPIC = "object-location";
    private ObjectLocation objLoc = new ObjectLocation("F372", "fuel_truck", 0.0, 0.0);
    private PulsarClient client = null;
    private Producer<ObjectLocation> producer = null;

    public App() {
    
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
            .topic("persistent://" + STREAM_NAME + "/" + NAMESPACE + "/" + TOPIC)
            .create();


            // Create consumer on a topic with a subscription
            // Consumer<GenericRecord> consumer = client.newConsumer(Schema.AUTO_CONSUME())
            // .topic("persistent://" + STREAM_NAME + "/" + NAMESPACE + "/" + TOPIC)
            // .subscriptionName("object-listener")
            // .subscribe();

            // ObjectLocation objLoc = new ObjectLocation("F372", "fuel_truck", 0.0, 0.0);
            // // Send a message to the topic
            // System.out.println("Sending a message...");
            // System.out.println(objLoc.toString());
            // producer.send(objLoc);
            // while(true) {
            //     // Every 5 seconds, update the object location
            //     objLoc = updatePosition(objLoc);
            //     System.out.println(objLoc.toString());
            //     producer.send(objLoc);
            // }

            // Consumer time!
            // System.out.println("Consumer: Looking for the message...");
            // boolean finished = false;
            // // Loop until a message is received
            // do {
            //     // Block for up to 1 second for a message
            //     Message<GenericRecord> msg  = consumer.receive(1, TimeUnit.SECONDS);

            //     if(msg != null){
            //         System.out.println("Message received!");
            //         GenericRecord gRec = msg.getValue();
            //         ObjectLocation recObj = new ObjectLocation(gRec);
            //         System.out.println(recObj.toString());

            //         // Acknowledge the message to remove it from the message backlog
            //         System.out.println("Acknowledging message...");
            //         consumer.acknowledge(msg);
            //     } else {
            //         // No more messages at the moment
            //         finished = true;
            //     }

            // } while (!finished);
        } catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void run() {
        // Send a message to the topic
        try {
            producer.send(objLoc);
            System.out.println(objLoc.toString());
            updatePosition(objLoc);
        } catch(PulsarClientException pcex) {
            pcex.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        Timer timer = new Timer();
        // Update the position every second
        timer.schedule(new App(), 0, 1000);
    }

    /**
     * Convert a byte[] into a human-readable String
     */
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * Animate the object location to match the following pattern
     * >>>>>>>>>>>>>>>>>
     * ^               |
     * ^               |
     * ^               |
     * ^<<<<<<<+       |
     *         ^       |
     *         ^       |
     *         ^       |
     *         <<<<<<<<<
     * @param objLoc
     * @return
     */
    static ObjectLocation updatePosition(ObjectLocation objLoc) {
        double leftEdge = -20.0;
        double topEdge = 20.0;
        double rightEdge = 20.0;
        double bottomEdge = -20.0;
        double movement = 1.0;

        if(objLoc.getX() == 0.0 && objLoc.getY() == 0.0) {
            // We are starting our animation. Start by moving towards the left edge
            // of our square
            objLoc.setX(-movement);
        } else if(objLoc.getX() < 0.0 && objLoc.getX() > leftEdge && objLoc.getY() == 0.0) {
            // Continue moving to the left edge of our area
            objLoc.setX(objLoc.getX() - movement);
        } else if(objLoc.getX() == leftEdge && objLoc.getY() < topEdge) {
            // We need to start moving north on our square patern
            objLoc.setY(objLoc.getY() + movement);
        } else if(objLoc.getY() == topEdge && objLoc.getX() < rightEdge) {
            // Continue moving right on the top edge
            objLoc.setX(objLoc.getX() + movement);
        } else if(objLoc.getX() == rightEdge && objLoc.getY() > bottomEdge) {
            // Keep moving down the right edge
            objLoc.setY(objLoc.getY() - movement);
        } else if(objLoc.getY() == bottomEdge && objLoc.getX() > 0.0) {
            // Keep moving left on the bottom edge
            objLoc.setX(objLoc.getX() - movement);
        } else {
            // We have reached the bottom-center of our square. Start moving up to 0, 0
            objLoc.setY(objLoc.getY() + movement);
        }
        return objLoc;
    }
}