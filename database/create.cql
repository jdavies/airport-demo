-- Create.cql
-- Create all tables used by the airline demo
use airport;

-- The object_location table shows the current location of every object in the airspace.
-- It does not track historical locations
-- Type may be any one of the following: ["misc", "bag_cart", "tractor", "fuel_truck", "aircraft", etc]
drop table if exists object_location;
create table object_location (
    name text,
    type text,
    x decimal,
    y decimal,
    PRIMARY KEY(name)
);

-- This table tracks the history of the objects location
drop table if exists object_location_history;
create table object_location_history (
    name text,
    type text,
    x decimal,
    y decimal,
    ts bigint,
    PRIMARY KEY((name), ts)
) WITH CLUSTERING ORDER BY (ts desc);

