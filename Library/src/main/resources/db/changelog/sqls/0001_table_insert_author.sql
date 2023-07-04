--liquibase formatted sql

--changeset Darshak:1

CREATE TABLE AUTHOR(
	AUTHOR_ID BIGINT,
	AUTHOR_NAME VARCHAR(16)
);