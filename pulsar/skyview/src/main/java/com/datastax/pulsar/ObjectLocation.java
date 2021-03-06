package com.datastax.pulsar;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.pulsar.client.api.schema.GenericRecord;

public class ObjectLocation implements Serializable {

    // The name of the object. For example: UA30405 (not a flight number, but an aircraft ID), or BagCart2687
    // This is the unique ID for the piece of equipment.
    public String equipment_id;

    // This need to be restricted to a list of values: ["misc", "aircraft", "fuel_truck", "bag_cart", "tractor"]
    public String equipment_type;

    // The x coordinate of the object
    public double x;

    // The y coordinate of the object
    public double y;

    // The current rotation of the object, measured i degrees, where 0 degrees is stright up.
    public double rotation;

    // This time when this object was in this location
    public long ts;

    public ObjectLocation(String equipmentID, String equipmentType, double x, double y, double degrees, long timestamp) {
        this.equipment_id = equipmentID;
        this.equipment_type = equipmentType;
        this.x = x;
        this.y = y;
        this.rotation = degrees;
        this.ts = timestamp;
    }

    // Constructor without the timestamp
    public ObjectLocation(String equipmentID, String equipmentType, double x, double y, double degrees) {
        this.equipment_id = equipmentID;
        this.equipment_type = equipmentType;
        this.x = x;
        this.y = y;
        this.rotation = degrees;
        this.ts = 0L;
    }

    /**
     * Construct an ObjectLocation record from the GenericRecord
     * from Pulsar
     * @param gRec
     */
    public ObjectLocation(GenericRecord gRec) {
        this.equipment_id = (String)gRec.getField("equipment_id");
        this.equipment_type = (String)gRec.getField("equipment_type");
        this.x = (double)gRec.getField("x");
        this.y = (double)gRec.getField("y");
        this.rotation = (double)gRec.getField("rotation");
        this.ts = (long)gRec.getField("ts");
    }

    public String toString() {
        Date date = new Date(ts);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
        return("Object: " + this.equipment_id + " - " + this.equipment_type + 
            " is located at " + x + ", " + y + ", rotated " + rotation + 
            "\u00B0 at " + df.format(date));
    }
}
