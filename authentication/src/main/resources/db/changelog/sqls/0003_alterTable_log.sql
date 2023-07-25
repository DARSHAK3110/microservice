--liquibase formatted sql

--changeset Darshak:3

ALTER TABLE log
DROP COLUMN action;
