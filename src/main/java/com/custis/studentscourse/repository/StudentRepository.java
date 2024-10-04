package com.custis.studentscourse.repository;

import com.custis.studentscourse.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Integer> {
}
