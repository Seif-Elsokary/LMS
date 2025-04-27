package com.question.learning_management_system.service.QuizService;

import com.question.learning_management_system.dto.QuizDto;
import com.question.learning_management_system.entity.Quiz;
import com.question.learning_management_system.repository.QuizRepository;
import com.question.learning_management_system.request.QuizRequest.CreateQuizRequest;
import com.question.learning_management_system.request.QuizRequest.UpdateQuizRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class QuizService implements IQuizService {
    private final QuizRepository quizRepository;
    private final ModelMapper modelMapper;

    @Override
    public QuizDto createQuiz(CreateQuizRequest request) {
        Quiz quiz = modelMapper.map(request, Quiz.class);
        quiz.setDifficulty(toUpperCase(quiz.getDifficulty()));
        Quiz savedQuiz = quizRepository.save(quiz);
        return convertToDto(savedQuiz);
    }

    @Override
    public QuizDto updateQuiz(Long quizId, UpdateQuizRequest request) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
        quiz.setTitle(request.getTitle());
        quiz.setDifficulty(toUpperCase(request.getDifficulty()));
        Quiz updatedQuiz = quizRepository.save(quiz);
        return convertToDto(updatedQuiz);
    }

    @Override
    public void deleteQuiz(Long quizId) {
        quizRepository.deleteById(quizId);
    }

    @Override
    public QuizDto getQuizById(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
        return convertToDto(quiz);
    }

    @Override
    public List<QuizDto> getAllQuizzes() {
        List<Quiz> quizzes = quizRepository.findAll();
        return convertToDtoList(quizzes);
    }

    @Override
    public List<QuizDto> getQuizzesByCourseId(Long courseId) {
        List<Quiz> quizzes = quizRepository.findByCourseId(courseId);
        return convertToDtoList(quizzes);
    }

    @Override
    public List<QuizDto> getQuizzesByDifficulty(Long studentId, String difficulty) {
        List<Long> courseIds = getStudentCourseIds(studentId);
        String normalizedDifficulty = toUpperCase(difficulty);
        List<Quiz> quizzes = quizRepository.findByCourseIdInAndDifficultyIgnoreCase(courseIds, normalizedDifficulty);
        return convertToDtoList(quizzes);
    }

    @Override
    public List<QuizDto> getQuizzesByCategory(Long studentId, String categoryName) {
        List<Long> courseIds = getStudentCourseIds(studentId);
        String normalizedCategory = splitAndNormalize(categoryName);
        List<Quiz> quizzes = quizRepository.findByCourseIdInAndCourseCategoryNameIgnoreCase(courseIds, normalizedCategory);
        return convertToDtoList(quizzes);
    }

    //============================================================ HELPER METHODS ============================================================

    private List<Long> getStudentCourseIds(Long studentId) {
        return quizRepository.findCourseIdsByStudentId(studentId);
    }

    private QuizDto convertToDto(Quiz quiz) {
        return modelMapper.map(quiz, QuizDto.class);
    }

    private List<QuizDto> convertToDtoList(List<Quiz> quizList) {
        return quizList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private String toUpperCase(String input) {
        return input != null ? input.toUpperCase() : null;
    }

    private String splitAndNormalize(String input) {
        if (input == null || input.isEmpty()) return input;
        String normalized = input.replaceAll("([a-z])([A-Z])", "$1 $2");
        return List.of(normalized.split("\\s+")).stream()
                .map(this::normalizeWord)
                .collect(Collectors.joining(" "));
    }

    private String normalizeWord(String word) {
        if (word == null || word.isEmpty()) return word;
        return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
    }
}
