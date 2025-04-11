package kz.bitlab.mainservice.mapper;

import kz.bitlab.mainservice.dto.ChapterRequest;
import kz.bitlab.mainservice.dto.ChapterResponse;
import kz.bitlab.mainservice.dto.CourseRequest;
import kz.bitlab.mainservice.dto.CourseResponse;
import kz.bitlab.mainservice.entity.Chapter;
import kz.bitlab.mainservice.entity.Course;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(builder = @Builder(disableBuilder = true))
public interface ChapterMapper {

    ChapterMapper INSTANCE = Mappers.getMapper(ChapterMapper.class);

    ChapterResponse toDto(Chapter chapter);

    Chapter toEntity (ChapterResponse response);

    Chapter toEntity(ChapterRequest request);

    List<ChapterResponse> toDtoList(List<Chapter> chapters);
}
