CREATE TABLE users
(
    id varchar primary key,
    name varchar not null,
    age int,
    products jsonb,
    profileimage varchar,
    connections jsonb
);