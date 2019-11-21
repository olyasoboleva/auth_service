create table user
(
    user_id     uuid not null
        constraint user_pk
            primary key,
    username    varchar(64)  not null unique ,
    password    varchar(128) not null,
    is_attendee boolean      not null
);