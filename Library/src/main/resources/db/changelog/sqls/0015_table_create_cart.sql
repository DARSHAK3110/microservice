--liquibase formatted sql

--changeset Darshak:15

CREATE TABLE CART(
	created_at DATETIME NOT NULL,
	deleted_at DATETIME,
	updated_at DATETIME,
	user_id BIGINT,
	book_details_id BIGINT,
	FOREIGN KEY(book_details_id) REFERENCES BOOK_DETAILS(id),
	FOREIGN KEY(user_id) REFERENCES USER(id),
	PRIMARY KEY(book_details_id,user_id)
);