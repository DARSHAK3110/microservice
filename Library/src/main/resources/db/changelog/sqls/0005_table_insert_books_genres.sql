--liquibase formatted sql

--changeset Darshak:5

CREATE TABLE BOOKS_GENRES(
	BOOK_GENRE_ID BIGINT PRIMARY KEY AUTO_INCREMENT,
	BOOK_ID BIGINT,
	GENRE_ID BIGINT,
	FOREIGN KEY (BOOK_ID) REFERENCES BOOKS(ISBN),
	FOREIGN KEY (GENRE_ID) REFERENCES GENRES(GENRE_ID)
);
