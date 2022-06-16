-- Create.cql
-- Create all tables used by the airline demo
use airport;

-- The object_location table shows the current location of every object in the airspace.
-- It does not track historical locations
-- Type may be any one of the following: ["misc", "bag_cart", "tractor", "fuel_truck", "aircraft", etc]
drop table if exists object_location;
create table object_location (
    equipment_id text,
    equipment_type text,
    rotation decimal,
    x decimal,
    y decimal,
    ts bigint,
    PRIMARY KEY(equipment_id)
);

-- This table tracks the history of the objects location
drop table if exists object_location_history;
create table object_location_history (
    equipment_id text,
    equipment_type text,
    x decimal,
    y decimal,
    rotation decimal,
    ts bigint,
    PRIMARY KEY((equipment_id), ts)
) WITH CLUSTERING ORDER BY (ts desc);

CREATE CUSTOM INDEX IF NOT EXISTS  x_loc
  ON object_location_history (x) USING 'StorageAttachedIndex';

CREATE CUSTOM INDEX IF NOT EXISTS  y_loc
  ON object_location_history (y) USING 'StorageAttachedIndex';

-- Testing Only
drop table if exists test_table;
create table test_table (
    equipment_id text,
    PRIMARY KEY(equipment_id)
);

select * from test_table;

-- Sample queries
select * from object_location_history where x > -15;
select * from object_location_history where x > -15 AND x < -10 AND y = 0;