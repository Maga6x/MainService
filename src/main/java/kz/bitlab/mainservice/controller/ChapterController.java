package kz.bitlab.mainservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.bitlab.mainservice.dto.ChapterRequest;
import kz.bitlab.mainservice.dto.ChapterResponse;
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
    @Operation(summary = "Получение списка глав", description = "Возвращает список всех глав")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Главы успешно получены", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ChapterResponse.class))
            }),
            @ApiResponse(responseCode = "500", description = "Ошибка при получении глав")
    })
    public ResponseEntity<List<ChapterResponse>> getChapters() {
        List<ChapterResponse> chapters = chapterService.getChapter();
        return ResponseEntity.ok(chapters);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение главы по ID", description = "Возвращает главу или ошибку")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Глава успешно найдена", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ChapterResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = "Глава не найдена")
    })
    public ResponseEntity<ChapterResponse> getChapterById(@PathVariable Long id) {
        ChapterResponse chapter = chapterService.getChapterById(id);
        return ResponseEntity.ok(chapter);
    }

    @PostMapping
    @Operation(summary = "Добавление главы", description = "Добавляет главу с проверкой на корректность")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Глава успешно создана"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос")
    })
    public ResponseEntity<Void> createChapter(@RequestBody ChapterRequest request) {
        chapterService.createChapter(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление главы по ID", description = "Удаляет главу по ID с проверкой наличия")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Глава успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Глава не найдена")
    })
    public ResponseEntity<Void> deleteChapterById(@PathVariable Long id) {
        chapterService.deleteChapterById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
