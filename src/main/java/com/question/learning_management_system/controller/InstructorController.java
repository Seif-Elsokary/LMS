package com.question.learning_management_system.controller;

import com.question.learning_management_system.dto.InstructorDto;

import com.question.learning_management_system.request.InstructorRequest.CreatInstructorRequest;
import com.question.learning_management_system.request.InstructorRequest.UpdateInstructorRequest;
import com.question.learning_management_system.service.InstructorService.IInstructorService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instructors")
@RequiredArgsConstructor
public class InstructorController {

    private final IInstructorService instructorService;

    @PostMapping
    public ResponseEntity<InstructorDto> addInstructor(@RequestBody CreatInstructorRequest request) {
        InstructorDto instructorDto = instructorService.addInstructor(request);
        return ResponseEntity.status(201).body(instructorDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InstructorDto> updateInstructor(@PathVariable Long id, @RequestBody UpdateInstructorRequest request) {
        InstructorDto instructorDto = instructorService.updateInstructor(request, id);
        return ResponseEntity.ok(instructorDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstructorDto> getInstructorById(@PathVariable Long id) {
        InstructorDto instructorDto = instructorService.findById(id);
        return ResponseEntity.ok(instructorDto);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<InstructorDto> getInstructorByEmail(@PathVariable String email) {
        InstructorDto instructorDto = instructorService.getInstructorByEmail(email);
        return ResponseEntity.ok(instructorDto);
    }

    @GetMapping
    public ResponseEntity<List<InstructorDto>> getAllInstructors() {
        List<InstructorDto> instructorDtos = instructorService.getAllInstructors();
        return ResponseEntity.ok(instructorDtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInstructor(@PathVariable Long id) {
        instructorService.deleteInstructor(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/email/{email}")
    public ResponseEntity<Void> deleteInstructorByEmail(@PathVariable String email) {
        instructorService.deleteInstructorByEmail(email);
        return ResponseEntity.noContent().build();
    }

}