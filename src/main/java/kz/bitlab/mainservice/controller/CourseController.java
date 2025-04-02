package kz.bitlab.mainservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.bitlab.mainservice.dto.CourseResponse;
import kz.bitlab.mainservice.exception.EntityNotFoundException;
import kz.bitlab.mainservice.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    @Operation(summary = "Получение списка курсов", description = "Возвращает списка всех курсов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "500", description = "Ошибка при получении курсов"),
            @ApiResponse(responseCode = "200", description = "Курс успешно получен", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CourseResponse.class))
            })
    })
    public ResponseEntity<List<CourseResponse>>getCourse(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description
    ){
        try {
            List<CourseResponse> recipes = courseService.getCourse(name, description);
            return ResponseEntity.ok(recipes);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение курса по ID", description = "Возвращает курс или ошибку")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Курс не найден"),
            @ApiResponse(responseCode = "200", description = "Курс успешно найден", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CourseResponse.class))
            })
    })
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable Long id){
        try {
            CourseResponse course = courseService.getCourseById(id);
            return new ResponseEntity<>(course, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
