package kz.bitlab.mainservice.mapper;

import kz.bitlab.mainservice.dto.ChapterRequest;
import kz.bitlab.mainservice.dto.ChapterResponse;
import kz.bitlab.mainservice.dto.LessonRequest;
import kz.bitlab.mainservice.dto.LessonResponse;
import kz.bitlab.mainservice.entity.Chapter;
import kz.bitlab.mainservice.entity.Lesson;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(builder = @Builder(disableBuilder = true))
public interface LessonMapper {

    LessonMapper INSTANCE = Mappers.getMapper(LessonMapper.class);

    LessonResponse toDto(Lesson lesson);

    Lesson toEntity (LessonResponse response);

    Lesson toEntity(LessonRequest request);

    List<LessonResponse> toDtoList(List<Lesson> lessons);
}
