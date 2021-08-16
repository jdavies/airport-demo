# Airport Demo

Demo of monitoring an airports airspace. All planes, gates, baggage carts, tractors, yellow gear, fuel trucks, etc.

## Architecture

The demo consists of three main parts:

1. FAA Events: This is a Pulsar instance that emulates the FAA events that are sent to each airport.
2. Database: This acts as a pulsar data-sinkto record all events
3. Application: This is a web application that is used to visualize the locations of all of the objects in the airspace. Later I would like to expand it to include windows that emulate the gate monitors.

In addition to these main parts will be a variety of tools that allow the demo presenter to create actions and influence the data flowing through pulsar. This will allow for things like gate changes for a flight, real-time baggage tracking on a customer application, etc.

### Planned Demo Scenarios

1. Watch the traffic: Be able to just watch all of the movemoent in an airport from an overhead view,allowing the viewer in zoom in and click on moving items to examine their details.
2. Modify a flights gate: Send the plane to a new gate and have the gate monitors and gate chane events fire off automatically.
3. Real Time Baggage Tracking: Allow a customer to see where their bag is (ie still in the plane, on the bagggage cart, getting loaded onto the conveyor belt or on the conveyer belt)

## Blog Entry

DataStax has recently released the Astra Streaming (read: managed Apache Pulsar) product to beta. You can create a free account at <https://astra.datastax.com> and begin testing it out. In this article, I will show you how to use Astra Streaming to not only feed information into the streaming "pipe", but also how to store those events into an Astra DB for later analisys.

In our scenario, we are using Astra Streaming to replicate the stream of object location information that is normally provided by the FAA to each airport. This stream reports the location of every piece of equipment at the airport (planes, fuel truck, aircraft tow tractors, baggage carts, etc). We want to see this information in two ways:

1. Where is everything located right now?
2. Where has a specific object been located historically? This is useful for tracking the paths of the object over time.

Instead of providing alot of theory up front, I'm going to dive right into the solution and explain things as I go along. Plu, the cude will help to answer alot of questions along the way. As always, you can access all of the source code for this project at 
