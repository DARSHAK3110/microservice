--liquibase formatted sql

--changeset Darshak:9

CREATE TABLE BOOK_STATUS(
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
	created_at DATETIME NOT NULL,
	deleted_at DATETIME,
	updated_at DATETIME,
	upload_id BIGINT,
	is_avaialable bit,
	location_id BIGINT,
	book_details_id BIGINT,
	FOREIGN KEY(book_details_id) REFERENCES BOOK_DETAILS(id),
	FOREIGN KEY(upload_id) REFERENCES UPLOAD(id),
	FOREIGN KEY(location_id) REFERENCES LOCATION(id)
);
