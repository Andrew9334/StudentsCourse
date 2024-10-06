CREATE TABLE IF NOT EXISTS course
(
    course_id        SERIAL PRIMARY KEY,
    course_name      VARCHAR(100) NOT NULL,
    total_seats      INT          NOT NULL,
    occupied_seats   INT DEFAULT 0,
    enrollment_start TIMESTAMP    NOT NULL,
    enrollment_end   TIMESTAMP    NOT NULL
);

CREATE TABLE IF NOT EXISTS student
(
    student_id   SERIAL PRIMARY KEY,
    student_name VARCHAR(100) NOT NULL
);

