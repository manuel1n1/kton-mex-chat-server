CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
create TABLE USERS (
    id uuid primary key DEFAULT uuid_generate_v4(),
    email varchar(100) unique,
    password varchar(100),
    created_at timestamp without time zone DEFAULT (now())::timestamp without time zone
);