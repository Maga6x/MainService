package kz.bitlab.mainservice.service;

import kz.bitlab.mainservice.dto.FileResponse;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    String uploadFile(MultipartFile file, Long lessonId);

    ByteArrayResource downloadFile(Long fileId);

    List<FileResponse> getFileList ();
}
