--liquibase formatted sql

--changeset Darshak:4

create table refresh_token(

refresh_token_id  bigint  primary key auto_increment,
user_id bigint  not null,
expire_at  datetime,
FOREIGN KEY (user_id) REFERENCES User(user_id)
);