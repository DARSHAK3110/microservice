--liquibase formatted sql

--changeset Darshak:12

ALTER TABLE BOOK_RESERVATION RENAME COLUMN is_avaialable TO is_accepted;