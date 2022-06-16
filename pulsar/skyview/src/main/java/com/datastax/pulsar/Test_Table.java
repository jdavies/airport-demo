package com.datastax.pulsar;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.pulsar.client.api.schema.GenericRecord;

public class Test_Table implements Serializable {

    // The name of the object. For example: UA30405 (not a flight number, but an aircraft ID), or BagCart2687
    // This is the unique ID for the piece of equipment.
    public String equipment_id;

    public String getEquipmentID() {
        return equipment_id;
    }

    public void setEquipmentID(String equipmentID) {
        this.equipment_id = equipmentID;
    }

    public Test_Table(String equipmentID) {
        this.equipment_id = equipmentID;
    }

    /**
     * Construct an ObjectLocation record from the GenericRecord
     * from Pulsar
     * @param gRec
     */
    public Test_Table(GenericRecord gRec) {
        this.equipment_id = (String)gRec.getField("equipment_id");
    }

    public String toString() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
        return("Object: equipment_id=" + this.equipment_id);
    }
}
