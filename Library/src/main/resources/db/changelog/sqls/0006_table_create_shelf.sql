--liquibase formatted sql

--changeset Darshak:6

CREATE TABLE SHELF(
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
	created_at DATETIME NOT NULL,
	deleted_at DATETIME,
	updated_at DATETIME,
	user_id BIGINT,
	section_id BIGINT,
	shelf_no BIGINT,
	FOREIGN KEY(section_id) REFERENCES SECTION(id),
	UNIQUE(section_id,shelf_no),
	FOREIGN KEY(user_id) REFERENCES USER(id)
);