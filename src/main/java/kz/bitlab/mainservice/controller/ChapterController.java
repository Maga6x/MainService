package kz.bitlab.mainservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.bitlab.mainservice.dto.ChapterRequest;
import kz.bitlab.mainservice.dto.ChapterResponse;
import kz.bitlab.mainservice.exception.EntityNotFoundException;
import kz.bitlab.mainservice.exception.EntityUniqueException;
import kz.bitlab.mainservice.service.ChapterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/chapter")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "ChapterController", description = "API для управления главами")
public class ChapterController {

    private final ChapterService chapterService;

    @GetMapping
    @Operation(summary = "Получение списка глав", description = "Возвращает списка всех глав")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "500", description = "Ошибка при получении главы"),
            @ApiResponse(responseCode = "200", description = "Глава успешно получен", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ChapterResponse.class))
            })
    })
    public ResponseEntity<List<ChapterResponse>> getChapters() {
        try {
            List<ChapterResponse> chapter = chapterService.getChapter();
            return ResponseEntity.ok(chapter);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/{id}")
    @Operation(summary = "Получение главы по ID", description = "Возвращает главу или ошибку")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Глава не найдена"),
            @ApiResponse(responseCode = "200", description = "Глава успешно найдена", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ChapterResponse.class))
            })
    })
    public ResponseEntity<ChapterResponse> getChapterById(@PathVariable Long id) {
        try {
            ChapterResponse chapter = chapterService.getChapterById(id);
            return ResponseEntity.ok(chapter);
        }catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    @Operation(summary = "Добавление Главы", description = "Добавляет главы проверив на корректность")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Неверный запрос"),
            @ApiResponse(responseCode = "201", description = "Глава успешно создана")
    })
    public ResponseEntity<Void> createChapter(@RequestBody ChapterRequest request) {
        try {
            chapterService.createChapter(request);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (EntityUniqueException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error while creating chapter {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление главы по ID", description = "Удалить главы по ID проверив на наличие")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Глава не найдена"),
            @ApiResponse(responseCode = "204", description = "Глава успешно удалена")
    })
    public ResponseEntity<Void> deleteChapterById(@PathVariable Long id) {
        try {
            chapterService.deleteChapterById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
