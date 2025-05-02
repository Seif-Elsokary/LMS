package com.question.learning_management_system.service.StudentService;

import com.question.learning_management_system.dto.StudentDto;
import com.question.learning_management_system.entity.Student;
import com.question.learning_management_system.enums.Role;
import com.question.learning_management_system.exception.StudentNotFoundException;
import com.question.learning_management_system.repository.CourseRepository;
import com.question.learning_management_system.repository.StudentRepository;
import com.question.learning_management_system.request.StudentRequest.CreateStudentRequest;
import com.question.learning_management_system.request.StudentRequest.UpdateStudentRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StudentService implements IStudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public StudentDto getStudentById(Long id) {
        return convertToStudentDto(findStudentById(id));
    }

    @Override
    public StudentDto getStudentByEmail(String email) {
        Student student = studentRepository.findByEmail(email);
        if (student == null) throw new StudentNotFoundException("Student with email " + email + " not found");
        return convertToStudentDto(student);
    }

    @Override
    public StudentDto getStudentByPhoneNumber(String phoneNumber) {
        Student student = studentRepository.findByPhoneNumber(phoneNumber);
        if (student == null) throw new StudentNotFoundException("Student with phone number " + phoneNumber + " not found");
        return convertToStudentDto(student);
    }

    @Override
    public List<StudentDto> getStudentsByName(String name) {
        return convertToStudentDtoList(studentRepository.findByName(name));
    }

    @Override
    public List<StudentDto> getStudentsByAge(int age) {
        return convertToStudentDtoList(studentRepository.findByAge(age));
    }

    @Override
    public List<StudentDto> getStudentsByNameAndAge(String name, int age) {
        return convertToStudentDtoList(studentRepository.findByNameAndAge(name, age));
    }

    @Override
    public List<StudentDto> getStudentsByGender(String gender) {
        return convertToStudentDtoList(studentRepository.findByGender(gender));
    }

    @Override
    public List<StudentDto> getStudentsByAgeAndGender(int age, String gender) {
        return convertToStudentDtoList(studentRepository.findByAgeAndGender(age, gender));
    }

    @Override
    public List<StudentDto> getAllStudents() {
        return convertToStudentDtoList(studentRepository.findAll());
    }

    @Transactional
    @Override
    public StudentDto addStudent(CreateStudentRequest request) {

        // تعيين الدور "USER" للطالب عند التسجيل
        Student newStudent = Student.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .age(request.getAge())
                .gender(request.getGender())
                .role(Role.USER)  // إضافة الدور كـ "USER"
                .build();

        return convertToStudentDto(studentRepository.save(newStudent));
    }

    @Override
    public StudentDto updateStudent(UpdateStudentRequest request, Long id) {
        validateGender(request.getGender());

        Student existingStudent = findStudentById(id);
        existingStudent.setName(request.getName());
        existingStudent.setAge(request.getAge());
        existingStudent.setGender(request.getGender().toUpperCase());
        existingStudent.setEmail(request.getEmail());
        existingStudent.setPhoneNumber(request.getPhoneNumber());
        existingStudent.setPassword(passwordEncoder.encode(request.getPassword()));

        return convertToStudentDto(studentRepository.save(existingStudent));
    }

    @Override
    public void deleteStudentById(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new StudentNotFoundException("Student with id " + id + " not found");
        }
        studentRepository.deleteById(id);
    }

    @Override
    public void deleteStudentByPhoneNumber(String phoneNumber) {
        if (!isExistsByPhoneNumber(phoneNumber)) {
            throw new StudentNotFoundException("Student with phone number " + phoneNumber + " not found");
        }
        studentRepository.deleteByPhoneNumber(phoneNumber);
    }

    @Override
    public boolean isExistsByEmail(String email) {
        return studentRepository.existsByEmail(email);
    }

    @Override
    public boolean isExistsByPhoneNumber(String phoneNumber) {
        return studentRepository.existsByPhoneNumber(phoneNumber);
    }

    @Override
    public boolean isExistsById(Long id) {
        return studentRepository.existsById(id);
    }

    private Student findStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student with id " + id + " not found"));
    }

    private StudentDto convertToStudentDto(Student student) {
        return modelMapper.map(student, StudentDto.class);
    }

    private List<StudentDto> convertToStudentDtoList(List<Student> students) {
        return students.stream()
                .map(this::convertToStudentDto)
                .collect(Collectors.toList());
    }

    private void validateGender(String gender) {
        if (!gender.equalsIgnoreCase("MALE") && !gender.equalsIgnoreCase("FEMALE")) {
            throw new IllegalArgumentException("Invalid gender: " + gender);
        }
    }
}
