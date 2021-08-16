package com.datastax.pulsar;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JSpinner.DateEditor;

import org.apache.pulsar.client.api.schema.GenericRecord;

import java.io.IOException;
import java.io.ObjectInputStream;

public class ObjectLocation implements Serializable {

    // The name of the object. For example: UA30405 (not a flight number, but an aircraft ID), or BagCart2687
    // This is the unique ID for the piece of equipment.
    private String name;

    // This need to be restricted to a list of values: ["misc", "aircraft", "fuel_truck", "bag_cart", "tractor"]
    private String type;

    // The x coordinate of the object
    private double x;

    // The y coordinate of the object
    private double y;

    private long timeStamp;

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

    public long getTimestamp() {
        return this.timeStamp;
    }

    public void setTimstamp(long newTS) {
        this.timeStamp = newTS;
    }

    public ObjectLocation(String name, String type, double x, double y, long timestamp) {
        this.name = name;
        this.type = type;
        this.x = x;
        this.y = y;
        this.timeStamp = timestamp;
    }

    /**
     * Construct an ObjectLocation record from the GenericRecord
     * from Pulsar
     * @param gRec
     */
    public ObjectLocation(GenericRecord gRec) {
        this.name = (String)gRec.getField("name");
        this.type = (String)gRec.getField("type");
        this.x = (double)gRec.getField("x");
        this.y = (double)gRec.getField("y");
        this.timeStamp = (long)gRec.getField("timestamp");
    }

    public String toString() {
        Date date = new Date(timeStamp);
        DateFormat df = new SimpleDateFormat("");
        return("Object: " + this.name + " - " + this.type +" is located at " + x + ", " + y + " at " + df.format(date));
    }

}
