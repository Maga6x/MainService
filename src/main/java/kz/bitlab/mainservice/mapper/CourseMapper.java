package kz.bitlab.mainservice.mapper;

import kz.bitlab.mainservice.dto.CourseRequest;
import kz.bitlab.mainservice.dto.CourseResponse;
import kz.bitlab.mainservice.entity.Course;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(builder = @Builder(disableBuilder = true))
public interface CourseMapper {

    CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);

    CourseResponse toDto(Course course);

    Course toEntity(CourseResponse dto);

    Course toEntity(CourseRequest dto);

    List<CourseResponse> toDtoList(List<Course> courses);
}
