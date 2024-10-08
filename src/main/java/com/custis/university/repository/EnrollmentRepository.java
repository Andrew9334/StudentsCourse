package com.custis.university.repository;

import com.custis.university.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {

    boolean existsByStudentIdAndCourseId(int studentId, int courseId);
}
