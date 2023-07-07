--liquibase formatted sql

--changeset Darshak:6

CREATE TABLE MEMBERS(
	MEMBER_ID BIGINT PRIMARY KEY AUTO_INCREMENT,
	NAME VARCHAR(16),
	ADDRESS VARCHAR(255),
	EMAIL VARCHAR(255) UNIQUE NOT NULL,
	PHONE BIGINT,
	UPLOAD_ID BIGINT,
	FOREIGN KEY(UPLOAD_ID) REFERENCES UPLOADS(UPLOAD_ID)
);