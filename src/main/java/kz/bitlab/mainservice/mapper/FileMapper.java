package kz.bitlab.mainservice.mapper;

import kz.bitlab.mainservice.dto.FileResponse;
import kz.bitlab.mainservice.entity.File;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FileMapper {

    FileResponse toDto(File file);

    List<FileResponse> toDtoList(List<File> files);
}