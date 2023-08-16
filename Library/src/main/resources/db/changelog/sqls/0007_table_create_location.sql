--liquibase formatted sql

--changeset Darshak:7

CREATE TABLE LOCATION(
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
	created_at DATETIME NOT NULL,
	deleted_at DATETIME,
	updated_at DATETIME,
	user_id BIGINT,
	shelf_id BIGINT,
	position VARCHAR(16),
	is_available BIT,
	FOREIGN KEY(shelf_id) REFERENCES SHELF(id),
	UNIQUE(shelf_id,position),
	FOREIGN KEY(user_id) REFERENCES USER(id)
);