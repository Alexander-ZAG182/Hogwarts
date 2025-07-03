-- liquibase formatted sql

-- changeset Alexander:1
CREATE TABLE IF NOT EXISTS Faculty (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    color VARCHAR(255) NOT NULL
);

-- changeset Alexander:2
CREATE TABLE IF NOT EXISTS Student (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    age INT NOT NULL,
    faculty_id BIGINT REFERENCES Faculty(id)
);