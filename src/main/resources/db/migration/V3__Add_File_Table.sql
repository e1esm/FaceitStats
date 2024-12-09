create table files(
    id serial primary key,
    s3_path text not null,
    created_at timestamp not null default now(),
    deleted_at timestamp
);