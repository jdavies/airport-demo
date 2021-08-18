package com.datastax.pulsar;

import java.util.Timer;
public class App {
    public static void main(String[] args)
    {
        Timer timer = new Timer();
        Flight flight1 = new Flight("ABS2045", "Boeing 737");
        // Update the position every second
        timer.schedule(flight1, 0, 1000);
    }
}