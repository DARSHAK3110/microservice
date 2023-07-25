--liquibase formatted sql

--changeset Darshak:2

create table log(

log_id  bigint  primary key auto_increment,
user_id bigint  not null,
action enum('LOGIN','LOGOUT') not null,
created_at  datetime,
FOREIGN KEY (user_id) REFERENCES User(user_id)
);