--liquibase formatted sql

--changeset Darshak:11

CREATE TABLE BOOK_BORROWING(
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
	created_at DATETIME NOT NULL,
	deleted_at DATETIME,
	updated_at DATETIME,
	borrower_id BIGINT,
	user_id BIGINT,
	book_status_id BIGINT,
	FOREIGN KEY(book_status_id) REFERENCES BOOK_STATUS(id),
	FOREIGN KEY(user_id) REFERENCES USER(id),
	FOREIGN KEY(borrower_id) REFERENCES USER(id)
);