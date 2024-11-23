CREATE TABLE groups
(
    id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    name  text not null,
    description text not null,
    icon text,
);