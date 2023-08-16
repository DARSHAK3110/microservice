--liquibase formatted sql

--changeset Darshak:3

CREATE TABLE UPLOAD(
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
	created_at DATETIME,
	file_name VARCHAR(16),
	user_id BIGINT,
	FOREIGN KEY(user_id) REFERENCES USER(id)
);

