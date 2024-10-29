package com.custis.university.repository;

import com.custis.university.model.Enrollment;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentRepository extends ReactiveCrudRepository<Enrollment, Integer> {

    boolean existsByStudentIdAndCourseId(int studentId, int courseId);
}
