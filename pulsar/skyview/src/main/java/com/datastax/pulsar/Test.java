package com.datastax.pulsar;

import java.util.TimerTask;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClientException;

public class Test extends TimerTask {

    private Test_Table test_table = null;  // Where is this aircraft located at currently?
    private Producer<Test_Table> producer = null;
    /**
     * 
     * @param equipment_id  The flight number. eg. "ABC2015"
     */
    public Test(Producer<Test_Table> producer, String equipment_id) {
        this.producer = producer;
        // Start life off of the screen
        test_table = new Test_Table(equipment_id);

    }


    public void run() {
        // Send a message to the topic
        try {
            if(test_table != null && producer!=null) {
                System.out.println("got it!");
                producer.send(test_table);
                System.out.println(test_table.toString());
            }
            
        } catch(PulsarClientException pcex) {
            pcex.printStackTrace();
        }
    }
}
