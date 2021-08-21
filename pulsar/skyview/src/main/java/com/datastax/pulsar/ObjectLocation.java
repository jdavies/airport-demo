package com.datastax.pulsar;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.pulsar.client.api.schema.GenericRecord;

public class ObjectLocation implements Serializable {

    // The name of the object. For example: UA30405 (not a flight number, but an aircraft ID), or BagCart2687
    // This is the unique ID for the piece of equipment.
    private String equipmentID;

    // This need to be restricted to a list of values: ["misc", "aircraft", "fuel_truck", "bag_cart", "tractor"]
    private String equipmentType;

    // The x coordinate of the object
    private double x;

    // The y coordinate of the object
    private double y;

    // The current rotation of the object, measured i degrees, where 0 degrees is stright up.
    private double rotation;

    public String getEquiipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }

    public String getEquipmentID() {
        return equipmentID;
    }

    public void setEquipmentID(String equipmentID) {
        this.equipmentID = equipmentID;
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

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double degrees) {
        this.rotation = degrees;
    }

    public ObjectLocation(String equipmentID, String equipmentType, double x, double y, double degrees) {
        this.equipmentID = equipmentID;
        this.equipmentType = equipmentType;
        this.x = x;
        this.y = y;
        this.rotation = degrees;
    }

    /**
     * Construct an ObjectLocation record from the GenericRecord
     * from Pulsar
     * @param gRec
     */
    public ObjectLocation(GenericRecord gRec) {
        this.equipmentID = (String)gRec.getField("equipmentID");
        this.equipmentType = (String)gRec.getField("equipmentType");
        this.x = (double)gRec.getField("x");
        this.y = (double)gRec.getField("y");
    }

    public String toString() {
        return("Object: " + this.equipmentID + " - " + this.equipmentType +" is located at " + x + ", " + y + ", rotated " + rotation + "\u00B0.");
    }
}
