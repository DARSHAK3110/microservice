--liquibase formatted sql

--changeset Darshak:4

ALTER TABLE user
ADD COLUMN email varchar(255) UNIQUE;
