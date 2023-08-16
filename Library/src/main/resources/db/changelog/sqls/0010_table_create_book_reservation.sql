--liquibase formatted sql

--changeset Darshak:10

CREATE TABLE BOOK_RESERVATION(
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
	created_at DATETIME NOT NULL,
	deleted_at DATETIME,
	updated_at DATETIME,
	reserve_id BIGINT,
	user_id BIGINT,
	is_avaialable bit,
	reservation_date DATE,
	book_details_id BIGINT,
	FOREIGN KEY(book_details_id) REFERENCES BOOK_DETAILS(id),
	FOREIGN KEY(user_id) REFERENCES USER(id),
	FOREIGN KEY(reserve_id) REFERENCES USER(id)
);
