--liquibase formatted sql

--changeset Darshak:16


ALTER TABLE BOOK_RESERVATION ADD book_status_id BIGINT;
ALTER TABLE BOOK_RESERVATION ADD FOREIGN KEY(book_status_id) REFERENCES BOOK_STATUS(id);
