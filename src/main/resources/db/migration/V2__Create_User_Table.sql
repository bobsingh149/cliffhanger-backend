CREATE TABLE users
(
    id TEXT primary key,
    name varchar not null,
    age int,
    bio TEXT,
    city TEXT,
    products UUID[] default '{}',
    liked_products UUID[] default '{}',
    commented_products TEXT[] default '{}',
    profile_image varchar,
    connections TEXT[] default '{}',
    interacted_users TEXT[] default '{}',
    requests jsonb default '[]'::jsonb,
    book_buddies jsonb default '[]'::jsonb,
    conversations jsonb default '[]'::jsonb

);