package com.datastax.pulsar;

import java.util.ArrayList;
import java.util.Date;
import java.util.TimerTask;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClientException;

public class Flight extends TimerTask {

    private ObjectLocation currentLocation = null;  // Where is this aircraft located at currently?
    private boolean isPaused = false;   // Is he animation paused or not
    private ArrayList<ObjectLocation> pathArray = new ArrayList<ObjectLocation>();
    private int animationFrame = 0; // This is an index into the pathArray.
    private Producer<ObjectLocation> producer = null;
    /**
     * 
     * @param equipment_id  The flight number. eg. "ABC2015"
     * @param aircraftType  Defines the type of aircraft. eg. "Boeing 737"
     * @param gateNumber May be a vaue from 1 to 3.
     */
    public Flight(Producer<ObjectLocation> producer, String equipment_id, String aircraftType, int gateNumber) {
        this.producer = producer;
        animationFrame = 0;
        initPath(equipment_id, aircraftType, gateNumber);

        // Initialize our location
        Date now = new Date();
        // Start life off of the screen
        currentLocation = new ObjectLocation(equipment_id, aircraftType, -20.0, -20.0, 0.0, now.getTime());
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
            if(currentLocation != null && producer!=null) {
                producer.send(currentLocation);
                System.out.println(currentLocation.toString());
                Date now = new Date();
                if(!isPaused) {
                    // Get our next position
                    ObjectLocation objLoc = pathArray.get(animationFrame++);
                    currentLocation = objLoc;
                }
                currentLocation.setTs(now.getTime());
            }
            
        } catch(PulsarClientException pcex) {
            pcex.printStackTrace();
        }
    }

    /**
     * There is a specific path to each of the 3 gates. They are defined here
     * @param equipment_id
     * @param equipmentType
     */
    void initPath(String equipment_id, String equipmentType, int gateNumber) {
        // Airport image is 960 x 720
        switch(gateNumber) {
            case 1:
                initGate1Path(equipment_id, equipmentType);
                break;

            case 2:
                initGate2Path(equipment_id, equipmentType);
                break;

            case 3:
                initGate3Path(equipment_id, equipmentType);
                break;

            default:
                System.out.println("Invaid gate number spcified: " + gateNumber);
        }
    }

    private void initGate1Path(String equipment_id, String equipmentType) {
        for(int x = 0; x< 70; x++){
            // Move from the left to the right at the top of the image
            pathArray.add(new ObjectLocation(equipment_id, equipmentType, 10.0 * x, 15.0, 90.0));
        }
        // Turn to the south
        pathArray.add(new ObjectLocation(equipment_id, equipmentType, 700.0, 15.0, 120.0));
        pathArray.add(new ObjectLocation(equipment_id, equipmentType, 700.0, 15.0, 150.0));
        pathArray.add(new ObjectLocation(equipment_id, equipmentType, 700.0, 15.0, 180.0));

        // Head south to get off of the run way
        for(int x = 0; x <5; x++) {
            pathArray.add(new ObjectLocation(equipment_id, equipmentType, 700.0, 10.0 * x + 15.0, 180.0));
        }

        // Turn to the west
        pathArray.add(new ObjectLocation(equipment_id, equipmentType, 700.0, 55.0, 210.0));
        pathArray.add(new ObjectLocation(equipment_id, equipmentType, 700.0, 55.0, 240.0));
        pathArray.add(new ObjectLocation(equipment_id, equipmentType, 700.0, 55.0, 270.0));

        // Head west to the gate to get off of the run way
        for(int x = 0; x <50; x++) {
            pathArray.add(new ObjectLocation(equipment_id, equipmentType, 700.0 - x * 10.0, 55.0, 270.0));
        }

        // Turn south for the final approach to the gate
        pathArray.add(new ObjectLocation(equipment_id, equipmentType, 200.0, 55.0, 240.0));
        pathArray.add(new ObjectLocation(equipment_id, equipmentType, 200.0, 55.0, 210.0));
        pathArray.add(new ObjectLocation(equipment_id, equipmentType, 200.0, 55.0, 180.0));

        // Approach the gate
        for(int x = 0; x < 10; x++) {
            pathArray.add(new ObjectLocation(equipment_id, equipmentType, 200.0, 55.0 + x * 10.0, 180.0));
        }
        
        // Stay at the gate for 100 cycles REDO!!!
        for(int x = 0; x < 100; x++) {
            pathArray.add(new ObjectLocation(equipment_id, equipmentType, 200.0, 155.0, 180.0));
        }

        // Back away from the gate

        // Disappear of the left side
    }

    private void initGate2Path(String equipment_id, String equipmentType) {
        for(int x = 0; x< 70; x++){
            // Move from the left to the right at the top of the image
            pathArray.add(new ObjectLocation(equipment_id, equipmentType, 10.0 * x, 15.0, 90.0));
        }
        // Turn to the south
        pathArray.add(new ObjectLocation(equipment_id, equipmentType, 700.0, 15.0, 120.0));
        pathArray.add(new ObjectLocation(equipment_id, equipmentType, 700.0, 15.0, 150.0));
        pathArray.add(new ObjectLocation(equipment_id, equipmentType, 700.0, 15.0, 180.0));
    }

    private void initGate3Path(String equipment_id, String equipmentType) {
        for(int x = 0; x< 70; x++){
            // Move from the left to the right at the top of the image
            pathArray.add(new ObjectLocation(equipment_id, equipmentType, 10.0 * x, 15.0, 90.0));
        }
        // Turn to the south
        pathArray.add(new ObjectLocation(equipment_id, equipmentType, 700.0, 15.0, 120.0));
        pathArray.add(new ObjectLocation(equipment_id, equipmentType, 700.0, 15.0, 150.0));
        pathArray.add(new ObjectLocation(equipment_id, equipmentType, 700.0, 15.0, 180.0));
    }
}
