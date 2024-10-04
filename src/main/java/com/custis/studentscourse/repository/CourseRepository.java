package com.custis.studentscourse.repository;

import com.custis.studentscourse.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Integer> {
}
