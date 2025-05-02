package com.question.learning_management_system.serviceTest.InstructorTest;

import com.question.learning_management_system.controller.InstructorController;
import com.question.learning_management_system.dto.InstructorDto;
import com.question.learning_management_system.request.InstructorRequest.CreateInstructorRequest;
import com.question.learning_management_system.request.InstructorRequest.UpdateInstructorRequest;
import com.question.learning_management_system.service.InstructorService.IInstructorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InstructorControllerTest {

    @InjectMocks
    private InstructorController instructorController;

    @Mock
    private IInstructorService instructorService;

    @Mock
    private InstructorDto instructorDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllInstructors() {
        List<InstructorDto> instructorDtos = List.of(instructorDto);
        when(instructorService.getAllInstructors()).thenReturn(instructorDtos);

        ResponseEntity<List<InstructorDto>> response = instructorController.getAllInstructors();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(instructorDtos, response.getBody());
    }

    @Test
    void testGetInstructorById() {
        when(instructorService.findById(1L)).thenReturn(instructorDto);

        ResponseEntity<InstructorDto> response = instructorController.getInstructorById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(instructorDto, response.getBody());
    }

    @Test
    void testGetInstructorByEmail() {
        when(instructorService.getInstructorByEmail("john@mail.com")).thenReturn(instructorDto);

        ResponseEntity<InstructorDto> response = instructorController.getInstructorByEmail("john@mail.com");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(instructorDto, response.getBody());
    }

    @Test
    void testAddInstructor() {
        CreateInstructorRequest request =  CreateInstructorRequest
                .builder()
                .name("Ali")
                .email("ali@mail.com")
                .phoneNumber("1234567890")
                .bio("Bio")
                .build();
        when(instructorService.addInstructor(request)).thenReturn(instructorDto);

        ResponseEntity<InstructorDto> response = instructorController.addInstructor(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(instructorDto, response.getBody());
    }

    @Test
    void testUpdateInstructor() {
        UpdateInstructorRequest request = UpdateInstructorRequest.builder()
                .name("Ali")
                .email("ali@mail.com")
                .phoneNumber("1234567890")
                .bio("Bio")
                .build();

        when(instructorService.updateInstructor(request, 1L)).thenReturn(instructorDto);

        ResponseEntity<InstructorDto> response = instructorController.updateInstructor(request, 1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(instructorDto, response.getBody());
    }

    @Test
    void testDeleteInstructor() {
        doNothing().when(instructorService).deleteInstructor(1L);

        ResponseEntity<Void> response = instructorController.deleteInstructor(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testDeleteInstructorByEmail() {
        doNothing().when(instructorService).deleteInstructorByEmail("john@mail.com");

        ResponseEntity<Void> response = instructorController.deleteInstructorByEmail("john@mail.com");
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testExistsByEmail() {
        when(instructorService.existsByEmail("john@mail.com")).thenReturn(true);

        ResponseEntity<Boolean> response = instructorController.existsByEmail("john@mail.com");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }

    @Test
    void testExistsByPhone() {
        when(instructorService.existsByPhone("0123456789")).thenReturn(true);

        ResponseEntity<Boolean> response = instructorController.existsByPhone("0123456789");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }

    @Test
    void testExistsById() {
        when(instructorService.existsById(1L)).thenReturn(true);

        ResponseEntity<Boolean> response = instructorController.existsById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }

    @Test
    void testCalculateInstructorRating() {
        when(instructorService.calculateInstructorRating(1L)).thenReturn(instructorDto);

        ResponseEntity<InstructorDto> response = instructorController.calculateInstructorRating(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(instructorDto, response.getBody());
    }
}
