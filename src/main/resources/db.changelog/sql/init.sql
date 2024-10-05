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

-- INSERT INTO course (course_name, total_seats, enrollment_start, enrollment_end)
-- VALUES ('Mathematics', 30, '2024-10-01T10:00:00Z', '2024-10-15T23:59:59Z'),
--        ('Physics', 25, '2024-10-01T10:00:00Z', '2024-10-15T23:59:59Z');

-- INSERT INTO student (student_name)
-- VALUES ('John Doe'),
--        ('Jane Smith');

