package com.custis.university.enrollment;

import com.custis.university.controller.EnrollmentController;
import com.custis.university.repository.CourseRepository;
import com.custis.university.repository.StudentRepository;
import com.custis.university.service.EnrollmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EnrollmentController.class)
@AutoConfigureMockMvc
public class EnrollmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private EnrollmentService enrollmentService;
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllCourses() throws Exception {
        mockMvc.perform(get("/api/enrollment/courses"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetCourse() throws Exception {
        mockMvc.perform(get("/api/enrollment/courses/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testEnrollStudent() throws Exception {
        mockMvc.perform(post("/api/enrollment")
                        .param("studentId", "1")
                        .param("courseId", "1"))
                .andExpect(status().isCreated());
    }
}
