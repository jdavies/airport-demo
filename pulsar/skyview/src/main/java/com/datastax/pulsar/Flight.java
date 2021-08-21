package com.datastax.pulsar;

import java.util.ArrayList;
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

    private ObjectLocationTimestamp objLocTs;  // Where is this aircraft located at currently?
    private boolean isPaused = false;   // Is he animation paused or not
    private ArrayList<ObjectLocation> pathArray = new ArrayList<ObjectLocation>();
    private int animationFrame = 0; // This is an index into the pathArray.

    /**
     * 
     * @param flightID  The flight number. eg. "ABC2015"
     * @param aircraftType  Defines the type of aircraft. eg. "Boeing 737"
     * @param gateNumber May be a vaue from 1 to 3.
     */
    public Flight(String flightID, String aircraftType, int gateNumber) {
        try {
            initPath(flightID, aircraftType, gateNumber);
            // Initialize our location
            Date now = new Date();
            // Start life off of the screen
            objLocTs = new ObjectLocationTimestamp(flightID, aircraftType, -20.0, -20.0, 0.0, now.getTime());
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

    /**
     * Pause the animation
     */
    public void pauseAnimation() {
        isPaused = true;
    }

    public void resumeAnimation() {
        isPaused = false;
    }

    public boolean isAnimationPaused() {
        return isPaused;
    }

    public void run() {
        // Send a message to the topic
        try {
            producer.send(objLocTs);
            System.out.println(objLocTs.toString());
            Date now = new Date();
            if(!isPaused) {
                updatePosition(objLocTs);
            }
            
            objLocTs.setTs(now.getTime());
        } catch(PulsarClientException pcex) {
            pcex.printStackTrace();
        }
    }

    /**
     * There is a specific path to each of the 3 gates. They are defined here
     * @param name
     * @param objectType
     */
    void initPath(String name, String objectType, int gateNumber) {
        // Airport image is 960 x 720
        switch(gateNumber) {
            case 1:
                initGate1Path(name, objectType);
                break;

            case 2:
                initGate2Path(name, objectType);
                break;

            case 3:
                initGate3Path(name, objectType);
                break;

            default:
                System.out.println("Invaid gate number spcified: " + gateNumber);
        }
    }

    private void initGate1Path(String flightID, String equipmentType) {
        for(int x = 0; x< 70; x++){
            // Move from the left to the right at the top of the image
            path1Array.add(new ObjectLocation(name, objectType, 10.0 * x, 15.0, 90.0));
        }
        // Turn to the south
        path1Array.add(new ObjectLocation(name, objectType, 700.0, 15.0, 120.0));
        path1Array.add(new ObjectLocation(name, objectType, 700.0, 15.0, 150.0));
        path1Array.add(new ObjectLocation(name, objectType, 700.0, 15.0, 180.0));

        // Head south to get off of the run way
        for(int x = 0; x <5; x++) {
            path1Array.add(new ObjectLocation(name, objectType, 700.0, 10.0 * x + 15.0, 180.0));
        }

        // Turn to the west
        path1Array.add(new ObjectLocation(name, objectType, 700.0, 55.0, 210.0));
        path1Array.add(new ObjectLocation(name, objectType, 700.0, 55.0, 240.0));
        path1Array.add(new ObjectLocation(name, objectType, 700.0, 55.0, 270.0));

        // Head west to the gate to get off of the run way
        for(int x = 0; x <50; x++) {
            path1Array.add(new ObjectLocation(name, objectType, 700.0 - x * 10.0, 55.0, 270.0));
        }

        // Turn south for the final approach to the gate
        path1Array.add(new ObjectLocation(name, objectType, 200.0, 55.0, 240.0));
        path1Array.add(new ObjectLocation(name, objectType, 200.0, 55.0, 210.0));
        path1Array.add(new ObjectLocation(name, objectType, 200.0, 55.0, 180.0));

        // Approach the gate
        for(int x = 0; x < 10; x++) {
            path1Array.add(new ObjectLocation(name, objectType, 200.0, 55.0 + x * 10.0, 180.0));
        }
        
        // Stay at the gate for 100 cycles REDO!!!
        for(int x = 0; x < 100; x++) {
            path1Array.add(new ObjectLocation(name, objectType, 200.0, 155.0, 180.0));
        }

        // Back away from the gate

        // Disappear of the left side
    }
}
