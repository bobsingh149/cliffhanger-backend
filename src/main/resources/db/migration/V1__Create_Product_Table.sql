CREATE TABLE prodcut
(
    id UUID primary key,
    owner_id UUID not null,
    name varchar(255) not null,
    description varchar,
    category varchar(255) not null ,
    image varchar
);