# Airport Demo

Demo of monitoring an airports airspace. All planes, gates, baggage carts, tractors, yellow gear, fuel trucks, etc.

## Changes from the Original Blog Entry

As this is a living project, it will undergo changes overtime. I have just made changes to both of the table definitions, changing the ```name``` field to ```equipment_id``` and the ```type``` field to ```equipment_type```. Please make a note of it.

## Architecture

The demo consists of three main parts:

1. Skyview: This is a Pulsar instance that emulates the FAA events that are sent to each airport.
2. Database: This acts as a pulsar data-sink to record all events
3. client_app: This is a web application that is used to visualize the locations of all of the objects in the airspace. Later I would like to expand it to include windows that emulate the gate monitors.

In addition to these main parts will be a variety of tools that allow the demo presenter to create actions and influence the data flowing through pulsar. This will allow for things like gate changes for a flight, real-time baggage tracking on a customer application, etc.

### Planned Demo Scenarios

1. Watch the traffic: Be able to just watch all of the movemoent in an airport from an overhead view,allowing the viewer in zoom in and click on moving items to examine their details.
2. Modify a flights gate: Send the plane to a new gate and have the gate monitors and gate chane events fire off automatically.
3. Real Time Baggage Tracking: Allow a customer to see where their bag is (ie still in the plane, on the bagggage cart, getting loaded onto the conveyor belt or on the conveyer belt)
