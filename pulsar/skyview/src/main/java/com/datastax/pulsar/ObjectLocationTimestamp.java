package com.datastax.pulsar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.pulsar.client.api.schema.GenericRecord;

public class ObjectLocationTimestamp extends ObjectLocation {
    // This time when this object was in this location
    private long ts;

    public long getTs() {
        return this.ts;
    }

    public void setTs(long newTS) {
        this.ts = newTS;
    }

    public ObjectLocationTimestamp(String name, String type, double x, double y, double degrees, long timestamp) {
        super(name, type, x, y, degrees);
        this.ts = timestamp;
    }

    /**
     * Construct an ObjectLocation record from the GenericRecord
     * from Pulsar
     * @param gRec
     */
    public ObjectLocationTimestamp(GenericRecord gRec) {
        super((String)gRec.getField("equipment_id"), (String)gRec.getField("equipment_type"), (double)gRec.getField("x"), (double)gRec.getField("y"), (double)gRec.getField("rotation"));
        this.ts = (long)gRec.getField("ts");
    }

    @Override
    public String toString() {
        Date date = new Date(ts);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
        return("Object: " + getEquipmentID() + " - " + getEquipmentType() +" is located at " + getX() + ", " + getY() + ", rotated " + getRotation() + "\u00B0 at " + df.format(date));
    }
}
