package com.question.learning_management_system.controller;

import com.question.learning_management_system.dto.InstructorDto;
import com.question.learning_management_system.request.InstructorRequest.CreateInstructorRequest;
import com.question.learning_management_system.request.InstructorRequest.UpdateInstructorRequest;
import com.question.learning_management_system.service.InstructorService.IInstructorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instructors")
@RequiredArgsConstructor
public class InstructorController {

    private final IInstructorService instructorService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<List<InstructorDto>> getAllInstructors() {
        return ResponseEntity.ok(instructorService.getAllInstructors());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<InstructorDto> getInstructorById(@PathVariable Long id) {
        return ResponseEntity.ok(instructorService.findById(id));
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<InstructorDto> getInstructorByEmail(@PathVariable String email) {
        return ResponseEntity.ok(instructorService.getInstructorByEmail(email));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<InstructorDto> addInstructor(@RequestBody CreateInstructorRequest request) {
        return ResponseEntity.ok(instructorService.addInstructor(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<InstructorDto> updateInstructor(@RequestBody UpdateInstructorRequest request, @PathVariable Long id) {
        return ResponseEntity.ok(instructorService.updateInstructor(request, id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteInstructor(@PathVariable Long id) {
        instructorService.deleteInstructor(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/email/{email}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteInstructorByEmail(@PathVariable String email) {
        instructorService.deleteInstructorByEmail(email);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists/email/{email}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Boolean> existsByEmail(@PathVariable String email) {
        return ResponseEntity.ok(instructorService.existsByEmail(email));
    }

    @GetMapping("/exists/phone/{phoneNumber}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Boolean> existsByPhone(@PathVariable String phoneNumber) {
        return ResponseEntity.ok(instructorService.existsByPhone(phoneNumber));
    }

    @GetMapping("/exists/id/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Boolean> existsById(@PathVariable Long id) {
        return ResponseEntity.ok(instructorService.existsById(id));
    }

    @GetMapping("/rating/{instructorId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<InstructorDto> calculateInstructorRating(@PathVariable Long instructorId) {
        return ResponseEntity.ok(instructorService.calculateInstructorRating(instructorId));
    }
}
