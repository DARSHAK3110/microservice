--liquibase formatted sql

--changeset Darshak:2

ALTER TABLE AUTHOR
MODIFY AUTHOR_ID BIGINT AUTO_INCREMENT PRIMARY KEY;

