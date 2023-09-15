--liquibase formatted sql

--changeset Darshak:4

ALTER TABLE user
MODIFY COLUMN email varchar(255) NOT NULL UNIQUE;
