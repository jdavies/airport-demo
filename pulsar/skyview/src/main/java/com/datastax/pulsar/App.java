package com.datastax.pulsar;

import java.util.Timer;
public class App {
    public App() {
        // try {
        //     // Create client object
        //     client = PulsarClient.builder()
        //     .serviceUrl(BROKER_SERVICE_URL)
        //     .authentication(
        //         AuthenticationFactory.token(Credentials.token)
        //     )
        //     .build();

        //     // Create producer on a topic
        //     producer = client.newProducer(JSONSchema.of(ObjectLocation.class))
        //     .topic("persistent://" + STREAM_NAME + "/" + NAMESPACE + "/" + TOPIC)
        //     .create();


        //     // Create consumer on a topic with a subscription
        //     // Consumer<GenericRecord> consumer = client.newConsumer(Schema.AUTO_CONSUME())
        //     // .topic("persistent://" + STREAM_NAME + "/" + NAMESPACE + "/" + TOPIC)
        //     // .subscriptionName("object-listener")
        //     // .subscribe();

        //     // ObjectLocation objLoc = new ObjectLocation("F372", "fuel_truck", 0.0, 0.0);
        //     // // Send a message to the topic
        //     // System.out.println("Sending a message...");
        //     // System.out.println(objLoc.toString());
        //     // producer.send(objLoc);
        //     // while(true) {
        //     //     // Every 5 seconds, update the object location
        //     //     objLoc = updatePosition(objLoc);
        //     //     System.out.println(objLoc.toString());
        //     //     producer.send(objLoc);
        //     // }

        //     // Consumer time!
        //     // System.out.println("Consumer: Looking for the message...");
        //     // boolean finished = false;
        //     // // Loop until a message is received
        //     // do {
        //     //     // Block for up to 1 second for a message
        //     //     Message<GenericRecord> msg  = consumer.receive(1, TimeUnit.SECONDS);

        //     //     if(msg != null){
        //     //         System.out.println("Message received!");
        //     //         GenericRecord gRec = msg.getValue();
        //     //         ObjectLocation recObj = new ObjectLocation(gRec);
        //     //         System.out.println(recObj.toString());

        //     //         // Acknowledge the message to remove it from the message backlog
        //     //         System.out.println("Acknowledging message...");
        //     //         consumer.acknowledge(msg);
        //     //     } else {
        //     //         // No more messages at the moment
        //     //         finished = true;
        //     //     }

        //     // } while (!finished);
        // } catch(Exception ex) {
        //     System.out.println(ex.getMessage());
        // }
    }

    // public void run() {
    //     // Send a message to the topic
    //     try {
    //         producer.send(objLoc);
    //         System.out.println(objLoc.toString());
    //         updatePosition(objLoc);
    //     } catch(PulsarClientException pcex) {
    //         pcex.printStackTrace();
    //     }
    // }

    public static void main(String[] args)
    {
        Timer timer = new Timer();
        Flight flight1 = new Flight("ABS2045", "Boeing 737");
        // Update the position every second
        timer.schedule(flight1, 0, 1000);
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
}