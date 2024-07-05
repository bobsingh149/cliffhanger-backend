CREATE TABLE users
(
    id varchar primary key,
    name varchar not null,
    age int,
    products jsonb,
    profile varchar,
    connections jsonb
);