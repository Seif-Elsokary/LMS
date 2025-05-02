package com.question.learning_management_system.controller;

import com.question.learning_management_system.configuration.jwt.JwtUtils;
import com.question.learning_management_system.configuration.user.LoginRequest;
import com.question.learning_management_system.configuration.user.UserDetailsService.CustomerUserDetails;
import com.question.learning_management_system.request.InstructorRequest.CreateInstructorRequest;
import com.question.learning_management_system.request.StudentRequest.CreateStudentRequest;
import com.question.learning_management_system.response.ApiResponse;

import com.question.learning_management_system.service.InstructorService.IInstructorService;
import com.question.learning_management_system.service.StudentService.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final IInstructorService instructorService;
    private final IStudentService studentService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils,
                          IInstructorService instructorService, IStudentService studentService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.instructorService = instructorService;
        this.studentService = studentService;
    }

    @PostMapping("/register/student")
    public ResponseEntity<ApiResponse<String>> registerStudent(@RequestBody CreateStudentRequest createStudentRequest) {
        try {
            studentService.addStudent(createStudentRequest);
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            createStudentRequest.getEmail(),
                            createStudentRequest.getPassword()
                    ));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtUtils.generateToken(authentication);

            ApiResponse<String> response = new ApiResponse<>(true, token, "Student registered and logged in successfully.");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            ApiResponse<String> response = new ApiResponse<>(false, null, "Error during registration: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/register/instructor")
    public ResponseEntity<ApiResponse<String>> registerInstructor(@RequestBody CreateInstructorRequest createInstructorRequest) {
        try {
            instructorService.addInstructor(createInstructorRequest);
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            createInstructorRequest.getEmail(),
                            createInstructorRequest.getPassword()
                    ));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtUtils.generateToken(authentication);

            ApiResponse<String> response = new ApiResponse<>(true, token, "Instructor registered and logged in successfully.");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            ApiResponse<String> response = new ApiResponse<>(false, null, "Error during registration: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    ));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtUtils.generateToken(authentication);
            CustomerUserDetails userDetails = (CustomerUserDetails) authentication.getPrincipal();

            ApiResponse<String> response = new ApiResponse<>(true, token, "Login successful");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (BadCredentialsException e) {
            ApiResponse<String> response = new ApiResponse<>(false, null, "Invalid credentials: Incorrect email or password.");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            ApiResponse<String> response = new ApiResponse<>(false, null, "Error during login: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
