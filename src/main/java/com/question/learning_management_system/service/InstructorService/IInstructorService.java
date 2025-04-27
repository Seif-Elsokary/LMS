package com.question.learning_management_system.service.InstructorService;

import com.question.learning_management_system.dto.InstructorDto;
import com.question.learning_management_system.request.InstructorRequest.CreatInstructorRequest;
import com.question.learning_management_system.request.InstructorRequest.UpdateInstructorRequest;

import java.util.List;

public interface IInstructorService {
    InstructorDto findById(Long id);
    List<InstructorDto> getAllInstructors();
    InstructorDto addInstructor(CreatInstructorRequest request);
    InstructorDto updateInstructor(UpdateInstructorRequest request , Long id);
    InstructorDto getInstructorByEmail(String email);
    void deleteInstructor(Long id);
    void deleteInstructorByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phoneNumber);
    boolean existsById(Long id);

}
