package com.question.learning_management_system.service.InstructorService;

import com.question.learning_management_system.dto.InstructorDto;
import com.question.learning_management_system.entity.Instructor;
import com.question.learning_management_system.exception.AlreadyExistsException;
import com.question.learning_management_system.exception.InstructorNotFoundException;
import com.question.learning_management_system.repository.InstructorRepository;
import com.question.learning_management_system.request.InstructorRequest.CreatInstructorRequest;
import com.question.learning_management_system.request.InstructorRequest.UpdateInstructorRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class InstructorService implements IInstructorService {

    private final InstructorRepository instructorRepository;
    private final ModelMapper modelMapper;

    @Override
    public InstructorDto findById(Long id) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new InstructorNotFoundException("Instructor with id: " + id + " not found!"));
        return convertToDto(instructor);
    }

    @Override
    public List<InstructorDto> getAllInstructors() {
        return instructorRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    public InstructorDto addInstructor(CreatInstructorRequest request) {
        validateInstructorUniqueness(request.getEmail(), request.getPhoneNumber());

        Instructor newInstructor = buildInstructorFromRequest(request);
        newInstructor = instructorRepository.save(newInstructor);

        log.info("Instructor with ID {} created successfully", newInstructor.getId());
        return convertToDto(newInstructor);
    }

    @Override
    public InstructorDto updateInstructor(UpdateInstructorRequest request, Long id) {
        Instructor existingInstructor = instructorRepository.findById(id)
                .orElseThrow(() -> new InstructorNotFoundException("Instructor with id: " + id + " not found!"));

        if (!existingInstructor.getEmail().equals(request.getEmail()) && existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException("Email already exists for another instructor.");
        }

        updateInstructorInfo(existingInstructor, request);
        instructorRepository.save(existingInstructor);

        log.info("Instructor with ID {} updated successfully", existingInstructor.getId());
        return convertToDto(existingInstructor);
    }

    @Override
    public InstructorDto getInstructorByEmail(String email) {
        Instructor instructor = instructorRepository.findByEmail(email);
        if (instructor == null) {
            throw new InstructorNotFoundException("Instructor with email: " + email + " not found!");
        }
        return convertToDto(instructor);
    }

    @Override
    public void deleteInstructor(Long id) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new InstructorNotFoundException("Instructor with id: " + id + " not found!"));
        instructorRepository.delete(instructor);

        log.info("Instructor with ID {} deleted successfully", id);
    }

    @Override
    public void deleteInstructorByEmail(String email) {
        Instructor instructor = instructorRepository.findByEmail(email);
        if (instructor == null) {
            throw new InstructorNotFoundException("Instructor with email: " + email + " not found!");
        }
        instructorRepository.delete(instructor);

        log.info("Instructor with email {} deleted successfully", email);
    }

    @Override
    public boolean existsByEmail(String email) {
        return instructorRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByPhone(String phoneNumber) {
        return instructorRepository.existsByPhoneNumber(phoneNumber);
    }

    @Override
    public boolean existsById(Long id) {
        return instructorRepository.existsById(id);
    }

    // -------------------- Private Helper Methods --------------------

    private void validateInstructorUniqueness(String email, String phone) {
        if (existsByEmail(email)) {
            throw new AlreadyExistsException("Instructor with email: " + email + " already exists!");
        }
        if (existsByPhone(phone)) {
            throw new AlreadyExistsException("Instructor with phone number: " + phone + " already exists!");
        }
    }

    private Instructor buildInstructorFromRequest(CreatInstructorRequest request) {
        return Instructor.builder()
                .name(request.getName())
                .email(request.getEmail())
                .bio(request.getBio())
                .phoneNumber(request.getPhoneNumber())
                .build();
    }

    private void updateInstructorInfo(Instructor instructor, UpdateInstructorRequest request) {
        instructor.setName(request.getName());
        instructor.setEmail(request.getEmail());
        instructor.setBio(request.getBio());
        instructor.setPhoneNumber(request.getPhoneNumber());
    }

    private InstructorDto convertToDto(Instructor instructor) {
        return modelMapper.map(instructor, InstructorDto.class);
    }
}
