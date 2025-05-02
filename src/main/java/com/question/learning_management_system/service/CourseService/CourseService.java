package com.question.learning_management_system.service.CourseService;

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
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CourseService implements ICourseService {

    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public CourseDto createCourse(CreateCourseRequest request) {

        Instructor instructor = instructorRepository.findById(request.getInstructorId())
                .orElseThrow(() -> new InstructorNotFoundException("Instructor with ID " + request.getInstructorId() + " not found"));

        Category category = categoryRepository.findByNameIgnoreCase(request.getCategoryName())
                .orElseGet(() -> {
                    Category newCategory = Category.builder().name(request.getCategoryName()).build();
                    return categoryRepository.save(newCategory);
                });

        Course course = Course.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .courseUrl(request.getCourseUrl())
                .instructor(instructor)
                .category(category)
                .build();

        Course savedCourse = courseRepository.save(course);
        return convertToCourseDto(savedCourse);
    }

    @Override
    public CourseDto updateCourse(UpdateCourseRequest request, long courseId) {
        return courseRepository.findById(courseId)
                .map(existingCourse -> {
                    existingCourse.setTitle(request.getTitle());
                    existingCourse.setDescription(request.getDescription());
                    existingCourse.setCourseUrl(request.getCourseUrl());
                    courseRepository.save(existingCourse);
                    return convertToCourseDto(existingCourse);
                }).orElseThrow(() -> new CourseNotFoundException("Course with ID " + courseId + " not found"));
    }

    @Override
    public CourseDto getCourseById(Long id) {
        return convertToCourseDto(
                courseRepository.findById(id)
                        .orElseThrow(() -> new CourseNotFoundException("Course with id: " + id + " Not Found!"))
        );
    }

    @Override
    public CourseDto getCourseByTitle(String title) {
        Course course = courseRepository.findByTitleIgnoreCase(title);
        if (course == null) {
            course = courseRepository.findByTitleMergedIgnoreCase(title);
        }
        if (course == null) {
            throw new CourseNotFoundException("Course with title: " + title + " Not Found!");
        }
        return convertToCourseDto(course);
    }

    @Override
    public List<CourseDto> getAllCourses() {
        return convertToCourseDtoList(courseRepository.findAll());
    }

    @Override
    public void deleteCourse(Long id) {
        courseRepository.findById(id)
                .ifPresentOrElse(courseRepository::delete, () -> {
                    throw new CourseNotFoundException("Course with id: " + id + " Not Found!");
                });
    }

    @Override
    public List<CourseDto> getCoursesByInstructorName(String name) {
        List<Course> courses = courseRepository.findByInstructor_NameIgnoreCase(name);

        if (courses.isEmpty()) {
            courses = courseRepository.findByInstructorNameMergedIgnoreCase(name);
        }

        return convertToCourseDtoList(courses);
    }

    @Override
    public List<CourseDto> getCoursesByCategory(Long categoryId) {
        return convertToCourseDtoList(courseRepository.findByCategoryId(categoryId));
    }

    @Override
    public List<CourseDto> getCoursesByInstructor(Long instructorId) {
        return convertToCourseDtoList(courseRepository.findByInstructorId(instructorId));
    }

    @Override
    public List<CourseDto> searchCoursesByMergedName(String mergedInput) {
        Course course = courseRepository.findByTitleMergedIgnoreCase(mergedInput);
        if (course != null) {
            return List.of(convertToCourseDto(course));
        }
        List<Course> instructorsCourses = courseRepository.findByInstructorNameMergedIgnoreCase(mergedInput);
        return convertToCourseDtoList(instructorsCourses);
    }

    @Override
    public List<CourseDto> getCoursesSortedByAverageRate() {
        List<Course> sortCoursesByRate = courseRepository.findAllCoursesSortedByAverageRate();
        return convertToCourseDtoList(sortCoursesByRate);
    }

    @Override
    public CourseDto getHighestRatedCourseByCategory(Long categoryId) {
        return convertToCourseDto(courseRepository.findCourseWithTopRatedByCategoryId(categoryId));
    }

    @Override
    public CourseDto getHighestRatedCourseByCategoryAndInstructorName(Long categoryId, String instructorName) {
        return convertToCourseDto(courseRepository.findTopCourseByCategoryAndInstructorNameIgnoreCase(categoryId , instructorName));
    }

    @Override
    public List<CourseDto> getHighestRatedCourseByCategoryAndInstructorName(String categoryName, String instructorName) {
        return convertToCourseDtoList(courseRepository.findTopCourseByCategoryNameAndInstructorNameIgnoreCase(categoryName , instructorName));
    }

    @Override
    public List<CourseDto> getCoursesByCategoryName(String categoryName) {
        return convertToCourseDtoList(courseRepository.findByCategoryNameIgnoreCase(categoryName));
    }

    @Override
    public CourseDto getHighestRatedCourseByCategoryName(String categoryName) {
        return convertToCourseDto(courseRepository.findCourseWithTopRatedByCategoryNameIgnoreCase(categoryName));
    }

    private CourseDto convertToCourseDto(Course course) {
        return modelMapper.map(course, CourseDto.class);
    }


    private List<CourseDto> convertToCourseDtoList(List<Course> courseList) {
        return courseList.stream()
                .map(this::convertToCourseDto)
                .collect(Collectors.toList());
    }
}
