CREATE TABLE product
(
    id varchar PRIMARY KEY,
    isbn bigint not null,
    userId UUID not null,
    works jsonb,
    title varchar(255) not null,
    authors jsonb not null,
    description varchar,
    image varchar,
    subjects jsonb,
    score bigint not null,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP not null
);


