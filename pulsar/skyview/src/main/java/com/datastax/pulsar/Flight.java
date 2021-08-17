package com.datastax.pulsar;

import java.util.Date;
import java.util.TimerTask;

import org.apache.pulsar.client.api.AuthenticationFactory;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.impl.schema.JSONSchema;


public class Flight extends TimerTask {
    private static final String BROKER_SERVICE_URL = "pulsar+ssl://pulsar-aws-useast2.streaming.datastax.com:6651";
    private static final String  STREAM_NAME = "airport-events";
    private static final String NAMESPACE = "airport";
    private static final String TOPIC = "object-location";

    private PulsarClient client = null;
    private Producer<ObjectLocation> producer = null;

    private ObjectLocation objLoc;  // Where is this aircraft located at currently?

    public Flight(String flightID, String aircraftType) {
        try {
            // Initialize our location
            Date now = new Date();
            objLoc = new ObjectLocation(flightID, aircraftType, 0.0, 0.0, now.getTime());
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
           
        } catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void run() {
        // Send a message to the topic
        try {
            producer.send(objLoc);
            System.out.println(objLoc.toString());
            Date now = new Date();
            updatePosition(objLoc);
            objLoc.setTs(now.getTime());
        } catch(PulsarClientException pcex) {
            pcex.printStackTrace();
        }
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
