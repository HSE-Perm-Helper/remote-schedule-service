-- liquibase formatted sql

-- changeset denismalinin:1730941262341-1
ALTER TABLE token
    ADD last_fetch TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP;

