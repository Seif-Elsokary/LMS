package com.question.learning_management_system.service.QuizService;

import com.question.learning_management_system.dto.QuizDto;
import com.question.learning_management_system.request.QuizRequest.CreateQuizRequest;
import com.question.learning_management_system.request.QuizRequest.UpdateQuizRequest;

import java.util.List;

public interface IQuizService {


    QuizDto createQuiz(CreateQuizRequest request);

    QuizDto updateQuiz(Long quizId, UpdateQuizRequest request);

    void deleteQuiz(Long quizId);

    QuizDto getQuizById(Long quizId);

    List<QuizDto> getAllQuizzes();

    List<QuizDto> getQuizzesByCourseId(Long courseId);

    List<QuizDto> getQuizzesByDifficulty(Long studentId, String difficulty);

    List<QuizDto> getQuizzesByCategory(Long studentId, String categoryName);
}
