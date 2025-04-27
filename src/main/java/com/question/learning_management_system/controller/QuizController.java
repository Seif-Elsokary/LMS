package com.question.learning_management_system.controller;

import com.question.learning_management_system.dto.QuizDto;
import com.question.learning_management_system.request.QuizRequest.CreateQuizRequest;
import com.question.learning_management_system.request.QuizRequest.UpdateQuizRequest;
import com.question.learning_management_system.service.QuizService.IQuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    private final IQuizService quizService;

    @PostMapping
    public ResponseEntity<QuizDto> createQuiz(@RequestBody CreateQuizRequest request) {
        QuizDto quizDto = quizService.createQuiz(request);
        return ResponseEntity.ok(quizDto);
    }

    @PutMapping("/{quizId}")
    public ResponseEntity<QuizDto> updateQuiz(@PathVariable Long quizId, @RequestBody UpdateQuizRequest request) {
        QuizDto quizDto = quizService.updateQuiz(quizId, request);
        return ResponseEntity.ok(quizDto);
    }

    @DeleteMapping("/{quizId}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable Long quizId) {
        quizService.deleteQuiz(quizId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{quizId}")
    public ResponseEntity<QuizDto> getQuizById(@PathVariable Long quizId) {
        QuizDto quizDto = quizService.getQuizById(quizId);
        return ResponseEntity.ok(quizDto);
    }

    @GetMapping
    public ResponseEntity<List<QuizDto>> getAllQuizzes() {
        List<QuizDto> quizzes = quizService.getAllQuizzes();
        return ResponseEntity.ok(quizzes);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<QuizDto>> getQuizzesByCourseId(@PathVariable Long courseId) {
        List<QuizDto> quizzes = quizService.getQuizzesByCourseId(courseId);
        return ResponseEntity.ok(quizzes);
    }

    @GetMapping("/difficulty")
    public ResponseEntity<List<QuizDto>> getQuizzesByDifficulty(@RequestParam Long studentId, @RequestParam String difficulty) {
        List<QuizDto> quizzes = quizService.getQuizzesByDifficulty(studentId, difficulty);
        return ResponseEntity.ok(quizzes);
    }

    @GetMapping("/category")
    public ResponseEntity<List<QuizDto>> getQuizzesByCategory(@RequestParam Long studentId, @RequestParam String categoryName) {
        List<QuizDto> quizzes = quizService.getQuizzesByCategory(studentId, categoryName);
        return ResponseEntity.ok(quizzes);
    }
}
