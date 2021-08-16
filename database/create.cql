-- Create.cql
-- Create all tables used by the airline demo
use airport;

-- The object_location table shows the current location of every object in the airspace.
-- It does not track historical locations
-- Type may be any one of the following: ["misc", "bag_cart", "tractor", "fuel_truck", "aircraft", etc]
create table object_location (
    id uuid,
    type text,
    name text,
    x decimal,
    y decimal,
    PRIMARY KEY(id)
);

