--liquibase formatted sql

--changeset Darshak:4

CREATE TABLE FLOOR(
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
	created_at DATETIME NOT NULL,
	deleted_at DATETIME,
	updated_at DATETIME,
	user_id BIGINT,
	floor_no BIGINT UNIQUE,
	FOREIGN KEY(user_id) REFERENCES USER(id)
);