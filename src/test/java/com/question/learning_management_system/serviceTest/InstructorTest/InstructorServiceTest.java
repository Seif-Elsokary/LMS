package com.question.learning_management_system.serviceTest.InstructorTest;

import com.question.learning_management_system.dto.InstructorDto;
import com.question.learning_management_system.entity.Course;
import com.question.learning_management_system.entity.Instructor;
import com.question.learning_management_system.exception.AlreadyExistsException;
import com.question.learning_management_system.exception.InstructorNotFoundException;
import com.question.learning_management_system.repository.CourseRepository;
import com.question.learning_management_system.repository.InstructorRepository;
import com.question.learning_management_system.request.InstructorRequest.CreateInstructorRequest;
import com.question.learning_management_system.request.InstructorRequest.UpdateInstructorRequest;
import com.question.learning_management_system.service.InstructorService.InstructorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InstructorServiceTest {

    @InjectMocks
    private InstructorService instructorService;

    @Mock
    private InstructorRepository instructorRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById_WhenExists() {
        Instructor instructor = Instructor.builder()
                .id(1L)
                .name("John")
                .email("john@mail.com")
                .phoneNumber("1234567890")
                .bio("Bio")
                .rating(0.0)
                .build();
        InstructorDto instructorDto = InstructorDto.builder().build();

        when(instructorRepository.findById(1L)).thenReturn(Optional.of(instructor));
        when(modelMapper.map(instructor, InstructorDto.class)).thenReturn(instructorDto);

        InstructorDto result = instructorService.findById(1L);
        assertNotNull(result);
        verify(instructorRepository).findById(1L);
    }

    @Test
    void testFindById_WhenNotExists() {
        when(instructorRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(InstructorNotFoundException.class, () -> instructorService.findById(1L));
    }

    @Test
    void testAddInstructor_WhenValid() {
        CreateInstructorRequest request =  CreateInstructorRequest
                .builder()
                .name("Ali")
                .email("ali@mail.com")
                .phoneNumber("1234567890")
                .bio("Bio")
                .build();
        Instructor savedInstructor = Instructor.builder()
                .id(1L)
                .name("Ali")
                .email("ali@mail.com")
                .phoneNumber("012345")
                .rating(0.0)
                .bio("Bio")
                .build();
        InstructorDto dto = InstructorDto.builder().build();

        when(instructorRepository.existsByEmail("ali@mail.com")).thenReturn(false);
        when(instructorRepository.existsByPhoneNumber("012345")).thenReturn(false);
        when(instructorRepository.save(any())).thenReturn(savedInstructor);
        when(modelMapper.map(savedInstructor, InstructorDto.class)).thenReturn(dto);

        InstructorDto result = instructorService.addInstructor(request);
        assertNotNull(result);
    }

    @Test
    void testAddInstructor_WhenEmailExists() {
        CreateInstructorRequest request =  CreateInstructorRequest
                .builder()
                .name("Ali")
                .email("ali@mail.com")
                .phoneNumber("1234567890")
                .bio("Bio")
                .build();
        when(instructorRepository.existsByEmail("ali@mail.com")).thenReturn(true);

        assertThrows(AlreadyExistsException.class, () -> instructorService.addInstructor(request));
    }

    @Test
    void testUpdateInstructor_Success() {
        UpdateInstructorRequest request = UpdateInstructorRequest.builder()
                .name("Ali")
                .email("ali@mail.com")
                .phoneNumber("1234567890")
                .bio("Bio")
                .build();
        Instructor instructor = Instructor.builder()
                .id(1L)
                .name("Old")
                .email("old@mail.com")
                .phoneNumber("000")
                .bio("Old bio")
                .rating(4.0)
                .build();
        InstructorDto dto = InstructorDto.builder().build();

        when(instructorRepository.findById(1L)).thenReturn(Optional.of(instructor));
        when(instructorRepository.existsByEmail("ali@mail.com")).thenReturn(false);
        when(modelMapper.map(instructor, InstructorDto.class)).thenReturn(dto);

        InstructorDto result = instructorService.updateInstructor(request, 1L);
        assertEquals(dto, result);
    }

    @Test
    void testUpdateInstructor_WhenEmailExists() {
        UpdateInstructorRequest request = UpdateInstructorRequest.builder()
                .name("Ali")
                .email("ali@mail.com")
                .phoneNumber("1234567890")
                .bio("Bio")
                .build();

        Instructor instructor = Instructor.builder()
                .id(1L)
                .name("Old")
                .email("old@mail.com")
                .phoneNumber("000")
                .bio("Old bio")
                .rating(4.0)
                .build();

        when(instructorRepository.findById(1L)).thenReturn(Optional.of(instructor));
        when(instructorRepository.existsByEmail("ali@mail.com")).thenReturn(true);

        assertThrows(AlreadyExistsException.class, () -> instructorService.updateInstructor(request, 1L));
    }

    @Test
    void testDeleteInstructor_WhenExists() {
        Instructor instructor = Instructor.builder().id(1L).build();
        when(instructorRepository.findById(1L)).thenReturn(Optional.of(instructor));

        instructorService.deleteInstructor(1L);
        verify(instructorRepository).delete(instructor);
    }

    @Test
    void testDeleteInstructor_WhenNotExists() {
        when(instructorRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(InstructorNotFoundException.class, () -> instructorService.deleteInstructor(1L));
    }

    @Test
    void testCalculateInstructorRating_WithCourses() {
        Instructor instructor = Instructor.builder().id(1L).build();
        Course course1 = Course.builder().averageRate(4.0).build();
        Course course2 = Course.builder().averageRate(5.0).build();

        when(instructorRepository.findById(1L)).thenReturn(Optional.of(instructor));
        when(courseRepository.findByInstructorId(1L)).thenReturn(List.of(course1, course2));
        when(instructorRepository.save(any())).thenReturn(instructor);
        when(modelMapper.map(instructor, InstructorDto.class)).thenReturn(InstructorDto.builder().build());

        InstructorDto result = instructorService.calculateInstructorRating(1L);
        assertNotNull(result);
        assertEquals(4.5, instructor.getRating());
    }

    @Test
    void testCalculateInstructorRating_WithNoCourses() {
        Instructor instructor = Instructor.builder().id(1L).build();

        when(instructorRepository.findById(1L)).thenReturn(Optional.of(instructor));
        when(courseRepository.findByInstructorId(1L)).thenReturn(List.of());
        when(instructorRepository.save(any())).thenReturn(instructor);
        when(modelMapper.map(instructor, InstructorDto.class)).thenReturn(InstructorDto.builder().build());

        InstructorDto result = instructorService.calculateInstructorRating(1L);
        assertNotNull(result);
        assertEquals(0.0, instructor.getRating());
    }

    @Test
    void testGetInstructorByEmail_WhenExists() {
        Instructor instructor = Instructor.builder()
                .id(1L)
                .name("John")
                .email("john@mail.com")
                .phoneNumber("1234567890")
                .bio("Bio")
                .rating(0.0)
                .build();
        InstructorDto instructorDto = InstructorDto.builder().build();

        when(instructorRepository.findByEmail("john@mail.com")).thenReturn(instructor);
        when(modelMapper.map(instructor, InstructorDto.class)).thenReturn(instructorDto);

        InstructorDto result = instructorService.getInstructorByEmail("john@mail.com");
        assertNotNull(result);
        verify(instructorRepository).findByEmail("john@mail.com");
    }

    @Test
    void testGetInstructorByEmail_WhenNotExists() {
        when(instructorRepository.findByEmail("john@mail.com")).thenReturn(null);
        assertThrows(InstructorNotFoundException.class, () -> instructorService.getInstructorByEmail("john@mail.com"));
    }
}
