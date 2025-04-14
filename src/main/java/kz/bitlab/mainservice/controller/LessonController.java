package kz.bitlab.mainservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.bitlab.mainservice.dto.LessonRequest;
import kz.bitlab.mainservice.dto.LessonResponse;
import kz.bitlab.mainservice.service.LessonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/lesson")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "LessonController", description = "API для управления уроками")
public class LessonController {

    private final LessonService lessonService;

    @GetMapping
    @Operation(summary = "Получение списка уроков", description = "Возвращает список всех уроков")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Уроки успешно получены", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = LessonResponse.class))
            }),
            @ApiResponse(responseCode = "500", description = "Ошибка при получении уроков")
    })
    public ResponseEntity<List<LessonResponse>> getLessons() {
        List<LessonResponse> lessons = lessonService.getLesson();
        return ResponseEntity.ok(lessons);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение урока по ID", description = "Возвращает урок или ошибку")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Урок успешно найден", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = LessonResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = "Урок не найден")
    })
    public ResponseEntity<LessonResponse> getLessonById(@PathVariable Long id) {
        LessonResponse lesson = lessonService.getLessonById(id);
        return ResponseEntity.ok(lesson);
    }

    @PostMapping
    @Operation(summary = "Добавление урока", description = "Добавляет урок с проверкой на уникальность")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Урок успешно создан"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос")
    })
    public ResponseEntity<Void> createLesson(@RequestBody LessonRequest request) {
        lessonService.createLesson(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление урока по ID", description = "Удаляет урок по ID с проверкой наличия")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Урок успешно удалён"),
            @ApiResponse(responseCode = "404", description = "Урок не найден")
    })
    public ResponseEntity<Void> deleteLessonById(@PathVariable Long id) {
        lessonService.deleteLessonById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
