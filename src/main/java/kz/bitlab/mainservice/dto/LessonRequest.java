package kz.bitlab.mainservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class LessonRequest {

    private String name;
    private String description;
    private Integer orderNum;
    private Chapter chapter;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
