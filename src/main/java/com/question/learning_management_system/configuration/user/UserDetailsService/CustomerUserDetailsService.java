package com.question.learning_management_system.configuration.user.UserDetailsService;

import com.question.learning_management_system.entity.Instructor;
import com.question.learning_management_system.entity.Student;
import com.question.learning_management_system.repository.InstructorRepository;
import com.question.learning_management_system.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomerUserDetailsService implements UserDetailsService {

    private final StudentRepository studentRepository;
    private final InstructorRepository instructorRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Student student = studentRepository.findByEmail(email);
        if(student!=null) {
            return CustomerUserDetails.fromStudent(student);
        }

        Instructor instructor = instructorRepository.findByEmail(email);
        if(instructor!=null) {
            return CustomerUserDetails.fromInstructor(instructor);
        }
        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}
