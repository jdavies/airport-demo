package com.datastax.pulsar;

import  java.util.UUID;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.IOException;

public class ObjectLocation implements Serializable {

    // The objects UUID
    private UUID id;

    //This need to be restricted to a list of values: ["misc", "aircraft", "fuel_truck", "bag_cart", "tractor"]
    private String type;

    // The name of the object. For example: UA30405 (not a flight number, but an aircraft ID), or BagCart2687
    private String name;

    // The x coordinate of the object
    private double x;

    // The y coordinate of the object
    private double y;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public ObjectLocation(UUID id, String type, String name, double x, double y) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public byte[] getBytes() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        byte[] yourBytes = null;
        try {
            out = new ObjectOutputStream(bos);   
            out.writeObject(this);
            out.flush();
            yourBytes = bos.toByteArray();
        } catch(IOException ex) {
            // Do nothing with this exception/
            ex.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
                // ignore close exception
                ex.printStackTrace();
            }
        }
        return yourBytes;
    }

}
