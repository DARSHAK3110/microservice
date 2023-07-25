--liquibase formatted sql

--changeset Darshak:3

ALTER TABLE refresh_token
ADD COLUMN token varchar(255);
