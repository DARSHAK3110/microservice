--liquibase formatted sql

--changeset Darshak:8

CREATE TABLE BOOK_DETAILS(
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
	created_at DATETIME NOT NULL,
	deleted_at DATETIME,
	updated_at DATETIME,
	upload_id BIGINT,
	available_copies BIGINT NOT NULL,
	total_copies BIGINT NOT NULL,
	isbn BIGINT NOT NULL UNIQUE,
	title VARCHAR(255),
	author_id BIGINT,
	FOREIGN KEY(author_id) REFERENCES AUTHOR(id),
	FOREIGN KEY(upload_id) REFERENCES UPLOAD(id)
);
