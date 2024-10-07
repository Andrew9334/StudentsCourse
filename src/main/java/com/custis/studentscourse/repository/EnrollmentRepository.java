package com.custis.studentscourse.repository;

import com.custis.studentscourse.model.Course;
import com.custis.studentscourse.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
}
