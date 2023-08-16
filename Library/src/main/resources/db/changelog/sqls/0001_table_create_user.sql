--liquibase formatted sql

--changeset Darshak:1

CREATE TABLE USER(
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
	phone BIGINT);
