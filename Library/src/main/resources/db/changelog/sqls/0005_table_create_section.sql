--liquibase formatted sql

--changeset Darshak:5

CREATE TABLE SECTION(
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
	created_at DATETIME NOT NULL,
	deleted_at DATETIME,
	updated_at DATETIME,
	user_id BIGINT,
	floor_id BIGINT,
	section_name VARCHAR(16),
	is_available BOOLEAN,
	FOREIGN KEY(floor_id) REFERENCES FLOOR(id),
	UNIQUE(floor_id,section_name),
	FOREIGN KEY(user_id) REFERENCES USER(id)
);