package kz.bitlab.mainservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.bitlab.mainservice.dto.CourseResponse;
import kz.bitlab.mainservice.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/course")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "CourseController", description = "API для управления курсами")
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    @Operation(summary = "Получение списка курсов", description = "Возвращает список всех курсов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Курсы успешно получены", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CourseResponse.class))
            }),
            @ApiResponse(responseCode = "500", description = "Ошибка при получении курсов")
    })
    public ResponseEntity<List<CourseResponse>> getCourse(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description
    ) {
        List<CourseResponse> recipes = courseService.getCourse(name, description);
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение курса по ID", description = "Возвращает курс или ошибку")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Курс успешно найден", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CourseResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = "Курс не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable Long id) {
        CourseResponse course = courseService.getCourseById(id);
        return ResponseEntity.ok(course);
    }
}
