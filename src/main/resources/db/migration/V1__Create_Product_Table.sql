CREATE TABLE product
(
    id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    userid TEXT not null,
    username TEXT not null,
    title TEXT not null,
    authors TEXT[] default '{}',
    cover_images TEXT[],
    description varchar,
    subjects TEXT[] default '{}',
    score bigint not null,
    caption TEXT,
    post_image TEXT,
    category TEXT,
    likes TEXT[] default '{}',
    comments jsonb default '[]'::jsonb,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP not null

);

