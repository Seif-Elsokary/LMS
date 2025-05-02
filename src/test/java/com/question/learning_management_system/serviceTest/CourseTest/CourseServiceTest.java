package com.question.learning_management_system.serviceTest.CourseTest;

import com.question.learning_management_system.dto.CourseDto;
import com.question.learning_management_system.entity.Category;
import com.question.learning_management_system.entity.Course;
import com.question.learning_management_system.entity.Instructor;
import com.question.learning_management_system.exception.CourseNotFoundException;
import com.question.learning_management_system.exception.InstructorNotFoundException;
import com.question.learning_management_system.repository.CategoryRepository;
import com.question.learning_management_system.repository.CourseRepository;
import com.question.learning_management_system.repository.InstructorRepository;
import com.question.learning_management_system.request.CourseRequest.CreateCourseRequest;
import com.question.learning_management_system.request.CourseRequest.UpdateCourseRequest;
import com.question.learning_management_system.service.CourseService.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private InstructorRepository instructorRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CourseService courseService;

    private CreateCourseRequest createRequest;
    private UpdateCourseRequest updateRequest;
    private Instructor instructor;
    private Category category;
    private Course course;
    private CourseDto courseDto;

    @BeforeEach
    void setup() {
        createRequest = new CreateCourseRequest();
        createRequest.setTitle("Java Basics");
        createRequest.setDescription("Intro to Java");
        createRequest.setCourseUrl("http://java-course");
        createRequest.setInstructorId(1L);
        createRequest.setCategoryName("Programming");

        updateRequest = new UpdateCourseRequest();
        updateRequest.setTitle("Advanced Java");
        updateRequest.setDescription("Deep dive into Java");
        updateRequest.setCourseUrl("http://advanced-java");

        instructor = Instructor.builder().id(1L).name("John").build();
        category = Category.builder().id(1L).name("Programming").build();

        course = Course.builder()
                .id(1L)
                .title("Java Basics")
                .description("Intro to Java")
                .courseUrl("http://java-course")
                .instructor(instructor)
                .category(category)
                .build();

        courseDto = new CourseDto();
        courseDto.setTitle("Java Basics");
    }

    @Test
    void testCreateCourse_Success() {
        when(instructorRepository.findById(1L)).thenReturn(Optional.of(instructor));
        when(categoryRepository.findByNameIgnoreCase("Programming")).thenReturn(Optional.of(category));
        when(courseRepository.save(any(Course.class))).thenReturn(course);
        when(modelMapper.map(any(Course.class), eq(CourseDto.class))).thenReturn(courseDto);

        CourseDto result = courseService.createCourse(createRequest);

        assertNotNull(result);
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void testCreateCourse_InstructorNotFound() {
        when(instructorRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(InstructorNotFoundException.class, () -> {
            courseService.createCourse(createRequest);
        });

        assertTrue(exception.getMessage().contains("Instructor with ID 1 not found"));
    }

    @Test
    void testGetCourseById_Success() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(modelMapper.map(any(Course.class), eq(CourseDto.class))).thenReturn(courseDto);

        CourseDto result = courseService.getCourseById(1L);

        assertNotNull(result);
        verify(courseRepository).findById(1L);
    }

    @Test
    void testGetCourseById_NotFound() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class, () -> {
            courseService.getCourseById(1L);
        });
    }

    @Test
    void testGetAllCourses() {
        when(courseRepository.findAll()).thenReturn(List.of(course));
        when(modelMapper.map(any(Course.class), eq(CourseDto.class))).thenReturn(courseDto);

        List<CourseDto> courses = courseService.getAllCourses();

        assertEquals(1, courses.size());
        verify(courseRepository).findAll();
    }

    @Test
    void testUpdateCourse_Success() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseRepository.save(any(Course.class))).thenReturn(course);
        when(modelMapper.map(course, CourseDto.class)).thenReturn(courseDto);

        CourseDto result = courseService.updateCourse(updateRequest, 1L);

        assertNotNull(result);
        verify(courseRepository).save(any(Course.class));
    }

    @Test
    void testUpdateCourse_NotFound() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class, () -> {
            courseService.updateCourse(updateRequest, 1L);
        });
    }

    @Test
    void testDeleteCourse_Success() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        courseService.deleteCourse(1L);

        verify(courseRepository).delete(course);
    }

    @Test
    void testDeleteCourse_NotFound() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class, () -> {
            courseService.deleteCourse(1L);
        });
    }
}
