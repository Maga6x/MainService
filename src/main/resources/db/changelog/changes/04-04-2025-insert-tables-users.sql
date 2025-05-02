-- liquibase formatted sql

-- changeset alisher:1
INSERT INTO users(username, full_name) VALUES ('student1', 'Student1');
INSERT INTO user_courses(user_id, course_id) VALUES (1, 1);