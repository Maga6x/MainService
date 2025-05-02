package kz.bitlab.mainservice.controller;

import kz.bitlab.mainservice.dto.FileResponse;
import kz.bitlab.mainservice.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/file")
public class FileController {

    private final FileService fileService;

    @PostMapping(value = "/upload")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<String> upload(@RequestParam MultipartFile file,
                                         @RequestParam Long lessonId) {

        return ResponseEntity.ok(fileService.uploadFile(file, lessonId));
    }

    @GetMapping(value = "/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable Long id) {
        ByteArrayResource resource = fileService.downloadFile(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=file")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @GetMapping(value = "/list")
    public List<FileResponse> fileList() {
        return fileService.getFileList();
    }

}
