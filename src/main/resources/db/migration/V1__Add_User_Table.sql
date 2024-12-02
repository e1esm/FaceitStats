create table users(
    id serial primary key,
    username text not null,
    password text not null,
    faceit_link text not null,
    role text not null,
    created_at timestamp not null
);