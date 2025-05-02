-- liquibase formatted sql

-- changeset alisher:1
-- comment создаю базу для файла
CREATE TABLE IF NOT EXISTS files (
    ID BIGSERIAL PRIMARY KEY,
    NAME VARCHAR(255),
    URL VARCHAR(255),
    LESSON_ID BIGINT,
    CREATED_TIME TIMESTAMP,
    CONSTRAINT file_lesson FOREIGN KEY (LESSON_ID) REFERENCES lessons(ID) ON DELETE CASCADE
    );

-- changeset alisher:2
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    full_name VARCHAR(255)
);

-- changeset alisher:3
CREATE TABLE user_courses (
    user_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, course_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);