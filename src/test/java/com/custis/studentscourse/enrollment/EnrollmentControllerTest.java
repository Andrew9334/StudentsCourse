package com.custis.studentscourse.enrollment;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.custis.studentscourse.controller.EnrollmentController;
import com.custis.studentscourse.repository.CourseRepository;
import com.custis.studentscourse.repository.EnrollmentRepository;
import com.custis.studentscourse.repository.StudentRepository;
import com.custis.studentscourse.service.EnrollmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(EnrollmentController.class)
@AutoConfigureMockMvc
public class EnrollmentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private CourseRepository courseRepository;
    private StudentRepository studentRepository;
    private EnrollmentRepository enrollmentRepository;
    private EnrollmentService enrollmentService;
    @BeforeEach
    public void setup() {
        courseRepository = Mockito.mock(CourseRepository.class);
        studentRepository = Mockito.mock(StudentRepository.class);
        enrollmentRepository = Mockito.mock(EnrollmentRepository.class);
        enrollmentService = new EnrollmentService(courseRepository, studentRepository, enrollmentRepository);
    }

    @Test
    public void testGetCourses() throws Exception {
        mockMvc.perform(get("/api/courses"))
                .andExpect(status().isOk());
    }

    @Test
    public void testEnrollStudent() throws Exception {
        mockMvc.perform(post("/api/enroll")
                        .param("studentId", "1")
                        .param("courseId", "1"))
                .andExpect(status().isOk());
    }
}
