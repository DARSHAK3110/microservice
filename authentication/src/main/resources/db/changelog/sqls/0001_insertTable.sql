--liquibase formatted sql

--changeset Darshak:1

create  table  User (  
    user_id  bigint  primary key auto_increment,  
    first_name  varchar(16)  not  null,
    last_name  varchar(16)  not  null,
    phone_number  bigint  not null UNIQUE,  
    role enum('ADMIN','USER') not null,
    password varchar(255)  not  null,
    created_at  datetime,
    updated_at datetime,
    deleted_at datetime
);
