# Airport Demo

Demo of monitoring an airports airspace. All planes, gates, baggage carts, tractors, yellow gear, fuel trucks, etc.

## Monitor the topic

```sh
bin/pulsar-client consume persistent://airport-events/airport/test-table -s njtest -p Earliest -st auto_consume -n 10
```

## Mapping Tested

equipment_id=value.equipment_id
FAILS

```sh
2022-06-15T16:43:22.799199939Z 16:43:22.799 [mapping-5] WARN  com.datastax.oss.sink.pulsar.CassandraSinkTask - Error decoding/mapping Pulsar record PulsarSinkRecord{SinkRecord(sourceRecord=PulsarRecord(topicName=Optional[persistent://airport-events/airport/test-table], partition=0, message=Optional[org.apache.pulsar.client.impl.MessageImpl@219946bf], schema=AUTO_CONSUME({schemaVersion=,schemaType=BYTES}{schemaVersion=\x00\x00\x00\x00\x00\x00\x00\x00,schemaType=JSON}{schemaVersion=org.apache.pulsar.common.protocol.schema.LatestVersion@70784eaf,schemaType=JSON}), failFunction=org.apache.pulsar.functions.source.PulsarSource$$Lambda$574/0x000000084078f040@21d6b650, ackFunction=org.apache.pulsar.functions.source.PulsarSource$$Lambda$573/0x000000084078fc40@42e1a6ef), value=org.apache.pulsar.client.impl.schema.generic.GenericJsonRecord@79c1e0ff)}: Primary key column equipment_id cannot be mapped to null. Check that your mapping setting matches your dataset contents.
```

equipment_id=key.equipment_id
FAILS

```sh
2022-06-15T16:22:49.399077966Z 16:22:49.398 [mapping-2] WARN  com.datastax.oss.sink.pulsar.CassandraSinkTask - Error decoding/mapping Pulsar record PulsarSinkRecord{SinkRecord(sourceRecord=PulsarRecord(topicName=Optional[persistent://airport-events/airport/test-table], partition=0, message=Optional[org.apache.pulsar.client.impl.MessageImpl@61c380d0], schema=AUTO_CONSUME({schemaVersion=,schemaType=BYTES}{schemaVersion=org.apache.pulsar.common.protocol.schema.LatestVersion@1b7a662,schemaType=JSON}{schemaVersion=\x00\x00\x00\x00\x00\x00\x00\x00,schemaType=JSON}), failFunction=org.apache.pulsar.functions.source.PulsarSource$$Lambda$534/0x0000000840747040@1f0b9afc, ackFunction=org.apache.pulsar.functions.source.PulsarSource$$Lambda$533/0x0000000840747c40@5d08777d), value=org.apache.pulsar.client.impl.schema.generic.GenericJsonRecord@48abd47e)}: Required field 'key.equipment_id' (mapped to column equipment_id) was missing from record (or may refer to an invalid function). Please remove it from the mapping.
```

key.equipment_id=value.equipment_id
FAILS - Status is reported as "Error - UNAVAILABLE io exception"

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
