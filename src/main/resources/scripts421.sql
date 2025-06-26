DROP TABLE IF EXISTS Avatar CASCADE;
DROP TABLE IF EXISTS Student CASCADE;
DROP TABLE IF EXISTS Faculty CASCADE;

CREATE TABLE Faculty (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    color VARCHAR(255) NOT NULL
);

CREATE TABLE Avatar (
    id SERIAL PRIMARY KEY,
    file_path VARCHAR(255),
    media_type VARCHAR(50),
    file_size BIGINT,
    data BYTEA
);

CREATE TABLE Student (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    age INT NOT NULL,
    faculty_id BIGINT REFERENCES Faculty(id),
    avatar_id BIGINT NULL
);

ALTER TABLE Student
    ADD CONSTRAINT age_check CHECK (age >= 16),
    ADD CONSTRAINT name_unique UNIQUE (name),
    ALTER COLUMN age SET DEFAULT 20;

ALTER TABLE Faculty
    ADD CONSTRAINT name_color_unique UNIQUE (name, color);