package com.question.learning_management_system.serviceTest.CourseTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.question.learning_management_system.controller.CourseController;
import com.question.learning_management_system.dto.CourseDto;
import com.question.learning_management_system.request.CourseRequest.CreateCourseRequest;
import com.question.learning_management_system.request.CourseRequest.UpdateCourseRequest;
import com.question.learning_management_system.service.CourseService.ICourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CourseControllerTest {

    @Mock
    private ICourseService courseService;
    @InjectMocks
    private CourseController courseController;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(courseController).build();
    }

    @Test
    public void  testGetAllCourses_Success() throws Exception {
        CourseDto course1 = new CourseDto(1L, "Java", "Programming", "Instructor A");
        CourseDto course2 = new CourseDto(2L, "Python", "Programming", "Instructor B");
        List<CourseDto> courseList = Arrays.asList(course1, course2);

        when(courseService.getAllCourses()).thenReturn(courseList);

        mockMvc.perform(get("/api/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Java"))
                .andExpect(jsonPath("$[1].title").value("Python"));
    }

    @Test
    public void testGetCourseByIdSuccess() throws Exception {
        CourseDto course1 = new CourseDto(1L, "Course 1", "Category 1", "Instructor 1");
        when(courseService.getCourseById(1L)).thenReturn(course1);

        mockMvc.perform(get("/api/courses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title")
                        .value("Course 1"));
    }

    @Test
    public void testCreateCourseSuccess() throws Exception {
        CreateCourseRequest request = CreateCourseRequest.builder()
                .title("Java Advanced")
                .description("Deep dive into Java topics")
                .courseUrl("http://example.com/java")
                .build();

        CourseDto course = new CourseDto(1L, "Course 1", "Category 1", "Instructor 1");
        when(courseService.createCourse(any(CreateCourseRequest.class))).thenReturn(course);

        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Course 1"));
    }

    @Test
    public void testUpdateCourseSuccess() throws Exception {
        UpdateCourseRequest request = UpdateCourseRequest.builder()
                .title("Java Advanced")
                .description("Deep dive into Java topics")
                .courseUrl("http://example.com/java")
                .build();

        CourseDto course = CourseDto.builder()
                .id(1L)
                .title("Course 2")
                .description("Deep dive into Java topics new course")
                .courseUrl("http://example.com/java1")
                .build();

        when(courseService.updateCourse(any(UpdateCourseRequest.class), eq(1L))).thenReturn(course);

        mockMvc.perform(put("/api/courses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Course 2"));
    }


    @Test
    public void testDeleteCourseSuccess() throws Exception {
        doNothing().when(courseService).deleteCourse(1L);

        mockMvc.perform(delete("/api/courses/1"))
                .andExpect(status().isNoContent());

        verify(courseService , times(1))
                .deleteCourse(1L);
    }

    @Test
    public void testGetCoursersByCategorySuccess() throws Exception {
        CourseDto course1 = CourseDto.builder()
                .id(1L)
                .title("Course 1")
                .description("Deep dive into Java topics")
                .courseUrl("http://example.com/java")
                .categoryName("Programming")
                .build();

        CourseDto course2 = CourseDto.builder()
                .id(2L)
                .title("Course 2")
                .description("Deep dive into Java topics part2")
                .categoryName("Programming")
                .build();

        List<CourseDto> courseList = Arrays.asList(course1, course2);
        when(courseService.getCoursesByCategory(1L)).thenReturn(courseList);
        mockMvc.perform(get("/api/courses/category/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Course 1"))
                .andExpect(jsonPath("$[1].title").value("Course 2"));
    }

    @Test
    public void testSearchCourseSuccess() throws Exception {
        CourseDto course1 = CourseDto.builder()
                .id(1L)
                .title("Course 1")
                .description("Deep dive into Java topics")
                .courseUrl("http://example.com/java")
                .categoryName("Programming")
                .build();

        CourseDto course2 = CourseDto.builder()
                .id(2L)
                .title("Course 2")
                .description("Deep dive into Java topics part2")
                .courseUrl("http://example.com/java2")
                .categoryName("Programming")
                .build();

        List<CourseDto> courseList = Arrays.asList(course1, course2);

        when(courseService.searchCoursesByMergedName("Java")).thenReturn(courseList);

        mockMvc.perform(get("/api/courses/search")
                        .param("query", "Java"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Course 1"))
                .andExpect(jsonPath("$[1].title").value("Course 2"));
    }

    @Test
    public void testGetCoursesByCategoryName() throws Exception {
        CourseDto course = CourseDto.builder()
                .id(1L)
                .title("Java Basics")
                .description("Intro to Java")
                .categoryName("Programming")
                .build();

        when(courseService.getCoursesByCategoryName("Programming"))
                .thenReturn(List.of(course));

        mockMvc.perform(get("/api/courses/category-name/Programming"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Java Basics"));
    }

    @Test
    public void testGetHighestRatedCourseByCategoryName() throws Exception {
        CourseDto course = CourseDto.builder()
                .id(1L)
                .title("Advanced Java")
                .description("Deep concepts")
                .categoryName("Programming")
                .build();

        when(courseService.getHighestRatedCourseByCategoryName("Programming"))
                .thenReturn(course);

        mockMvc.perform(get("/api/courses/highest-rated/category-name/Programming"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Advanced Java"));
    }

    @Test
    public void testSearchCoursesByMerged() throws Exception {
        CourseDto course = CourseDto.builder()
                .id(1L)
                .title("Spring Boot Course")
                .description("Spring Boot details")
                .categoryName("Backend")
                .build();

        when(courseService.searchCoursesByMergedName("Spring"))
                .thenReturn(List.of(course));

        mockMvc.perform(get("/api/courses/search-merged")
                        .param("mergedInput", "Spring"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Spring Boot Course"));
    }

    @Test
    public void testGetHighestRatedCourseByCategoryId() throws Exception {
        CourseDto course = CourseDto.builder()
                .id(1L)
                .title("Algorithms")
                .description("Data structures and algorithms")
                .categoryName("CS")
                .build();

        when(courseService.getHighestRatedCourseByCategory(5L))
                .thenReturn(course);

        mockMvc.perform(get("/api/courses/highest-rated/category/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Algorithms"));
    }

    @Test
    public void testGetHighestRatedCourseByCategoryAndInstructor() throws Exception {
        CourseDto course = CourseDto.builder()
                .id(1L)
                .title("React Course")
                .description("Frontend mastery")
                .categoryName("Frontend")
                .build();

        when(courseService.getHighestRatedCourseByCategoryAndInstructorName("Frontend", "John"))
                .thenReturn(List.of(course));

        mockMvc.perform(get("/api/courses/highest-rated/category/Frontend/instructor/John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("React Course"));
    }

    @Test
    public void testGetCoursesSortedByRate() throws Exception {
        CourseDto course = CourseDto.builder()
                .id(1L)
                .title("Docker Course")
                .description("Containerization explained")
                .categoryName("DevOps")
                .build();

        when(courseService.getCoursesSortedByAverageRate())
                .thenReturn(List.of(course));

        mockMvc.perform(get("/api/courses/courses-sorted-by-rate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Docker Course"));
    }
}
