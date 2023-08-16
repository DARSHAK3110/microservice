--liquibase formatted sql

--changeset Darshak:13

ALTER TABLE BOOK_STATUS RENAME COLUMN is_avaialable TO is_available;