-- liquibase formatted sql

-- changeset alisher:1
INSERT INTO courses (name, description, created_time, updated_time)
VALUES
    ('Java Developer - Core', 'QUESTIONS_BANK', NOW(),NOW()),
    ('Java Developer - Spring Boot', 'JAVA DEVELOPER', NOW(),NOW()),
    ('Java Developer - Spring Enterprise', 'JAVA DEVELOPER', NOW(),NOW()),
    ('Java Developer - Spring Pro', 'JAVA DEVELOPER', NOW(),NOW());

-- changeset alisher:2
INSERT INTO chapters(name, description, order_num, course_id, created_time, updated_time)
VALUES
    ('Общее','Объявления', 1, 1, NOW(),NOW()),
    ('Неделя 9: Обработка исключений (try-catch)', 'выавыа', 2, 1, NOW(),NOW()),
    ('Неделя 10: Коллекции. Map, HashMap','dsfdsfds', 3,1, NOW(),NOW()),
    ('Неделя 13: Spring Boot, пер', 'wqeqw',1,2, NOW(),NOW()),
    ('Неделя 14: Работа с Thymele', 'dsfdsf',2,2, NOW(),NOW()),
    ('Неделя 14: Dependency I','wqe',3,2, NOW(),NOW());

-- changeset alisher:3
INSERT INTO lessons(name, description, content, order_num, chapter_id, created_time, updated_time)
VALUES
    ('Обработка исключений (try-catch)', 'dsfsdf','dfsdfsd',1,1, NOW(),NOW()),
    ('Контроллеры. Отправка и обработка HTTP запросов, @RequestParam', 'sdfsdf','asdasd',1,4, NOW(),NOW()),
    ('Практическое задание','fsdfsd','asdsad',2,4, NOW(),NOW()),
    ('Thymeleaf Layout Dialect','dfsdf', 'sadasd',1,5, NOW(),NOW());


