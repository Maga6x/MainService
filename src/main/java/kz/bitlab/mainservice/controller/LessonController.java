package kz.bitlab.mainservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.bitlab.mainservice.dto.LessonRequest;
import kz.bitlab.mainservice.dto.LessonResponse;
import kz.bitlab.mainservice.exception.EntityNotFoundException;
import kz.bitlab.mainservice.exception.EntityUniqueException;
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
@Tag(name = "LessonController", description = "API для управления уроков")
public class LessonController {

    private final LessonService lessonService;

    @GetMapping
    @Operation(summary = "Получение списка уроков", description = "Возвращает списка всех уроков")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "500", description = "Ошибка при получении урока"),
            @ApiResponse(responseCode = "200", description = "Урок успешно получен", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = LessonResponse.class))
            })
    })
    public ResponseEntity<List<LessonResponse>> getLessons() {
        try {
            List<LessonResponse> lesson = lessonService.getLesson();
            return ResponseEntity.ok(lesson);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/{id}")
    @Operation(summary = "Получение урока по ID", description = "Возвращает урока или ошибку")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Урок не найден"),
            @ApiResponse(responseCode = "200", description = "Урок успешно найден", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = LessonResponse.class))
            })
    })
    public ResponseEntity<LessonResponse> getLessonById(@PathVariable Long id) {
        try {
            LessonResponse lesson = lessonService.getLessonById(id);
            return ResponseEntity.ok(lesson);
        }catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    @Operation(summary = "Добавление Урока", description = "Добавляет Урок проверив на корректность")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Неверный запрос"),
            @ApiResponse(responseCode = "201", description = "Урок успешно создан")
    })
    public ResponseEntity<Void> createChapter(@RequestBody LessonRequest request) {
        try {
            lessonService.createLesson(request);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (EntityUniqueException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error while creating lesson {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление урока по ID", description = "Удалить урок по ID проверив на наличие")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Урок не найдена"),
            @ApiResponse(responseCode = "204", description = "Урок успешно удалена")
    })
    public ResponseEntity<Void> deleteLessonById(@PathVariable Long id) {
        try {
            lessonService.deleteLessonById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
