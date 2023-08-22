--liquibase formatted sql

--changeset Darshak:16

ALTER TABLE BOOK_STATUS ADD is_reserved bit;