package kz.bitlab.mainservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kz.bitlab.mainservice.entity.Chapter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "DTO для работы с Уроками")
public class LessonResponse {

    private Long id;
    private String name;
    private String description;
    private Integer orderNum;
    private Chapter chapter;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
