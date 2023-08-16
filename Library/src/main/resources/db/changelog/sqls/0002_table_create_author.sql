--liquibase formatted sql

--changeset Darshak:2

CREATE TABLE AUTHOR(
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
	created_at DATETIME NOT NULL,
	deleted_at DATETIME,
	updated_at DATETIME,
	user_id BIGINT,
	name VARCHAR(16),
	date_of_birth DATE,
	FOREIGN KEY(user_id) REFERENCES USER(id)
);
	