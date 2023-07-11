--liquibase formatted sql

--changeset Darshak:10

ALTER TABLE BOOKS_GENRES ADD PRIMARY KEY(BOOK_ID, GENRE_ID);

