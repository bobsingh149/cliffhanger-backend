CREATE TABLE users
(
    id UUID primary key,
    name varchar not null,
    age int,
    products TEXT[] default '{}',
    profileimage varchar,
    connections UUID[] default '{}',
    requests UUID[] default '{}'
);