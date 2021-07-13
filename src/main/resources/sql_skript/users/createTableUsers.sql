create table users
(
    id         int auto_increment
    primary key,
    email      varchar(255)                 not null,
    user_name varchar(255)                  not null,
    password   varchar(255)                 not null,
    constraint users_email_uindex
        unique (email)
);