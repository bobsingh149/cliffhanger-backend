CREATE TABLE product
(
    id varchar PRIMARY KEY,
    isbn bigint not null,
    userId UUID not null,
    works jsonb,
    title varchar(255) not null,
    authors TEXT[] default '{}',
    description varchar,
    image varchar,
    subjects TEXT[] default '{}',
    score bigint not null,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP not null
);


