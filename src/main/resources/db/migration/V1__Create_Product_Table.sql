CREATE TABLE product
(
    id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    userId TEXT not null,
    title TEXT not null,
    authors TEXT[] default '{}',
    coverimages TEXT[],
    description varchar,
    subjects TEXT[] default '{}',
    score bigint not null,
    caption TEXT,
    postimage TEXT,
    category TEXT,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP not null

);

