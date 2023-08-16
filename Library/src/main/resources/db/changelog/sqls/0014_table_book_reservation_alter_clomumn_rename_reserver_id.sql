--liquibase formatted sql

--changeset Darshak:14

ALTER TABLE BOOK_RESERVATION RENAME COLUMN reserve_id TO reserver_id;