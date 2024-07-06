CREATE TABLE users
(
    id UUID primary key,
    name varchar not null,
    age int,
    products jsonb,
    profileimage varchar,
    connections jsonb
);