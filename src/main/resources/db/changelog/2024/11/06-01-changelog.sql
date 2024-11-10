-- liquibase formatted sql

-- changeset denismalinin:1730918824239-1
CREATE TABLE token
(
    telegram_id BIGINT      NOT NULL,
    token       VARCHAR(64) NOT NULL,
    CONSTRAINT pk_token PRIMARY KEY (telegram_id)
);

