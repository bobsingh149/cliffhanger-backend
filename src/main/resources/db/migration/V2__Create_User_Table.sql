CREATE TABLE users
(
    id TEXT primary key,
    name varchar not null,
    age int,
    products TEXT[] default '{}',
    profileimage varchar,
    connections TEXT[] default '{}',
    requests TEXT[] default '{}'
);