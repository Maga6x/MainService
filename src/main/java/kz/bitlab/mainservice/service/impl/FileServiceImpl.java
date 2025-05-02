package kz.bitlab.mainservice.service.impl;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import kz.bitlab.mainservice.dto.FileResponse;
import kz.bitlab.mainservice.entity.File;
import kz.bitlab.mainservice.entity.Lesson;
import kz.bitlab.mainservice.mapper.FileMapper;
import kz.bitlab.mainservice.repository.FileRepository;
import kz.bitlab.mainservice.repository.LessonRepository;
import kz.bitlab.mainservice.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {

    private final MinioClient minioClient;
    private final FileRepository fileRepository;
    private final LessonRepository lessonRepository;
    private final FileMapper fileMapper;

    @Value("${minio.bucket}")
    private String bucket;

    @Override
    public String uploadFile(MultipartFile file, Long lessonId) {
        try {
            Lesson lesson = lessonRepository.findById(lessonId)
                    .orElseThrow(() -> new RuntimeException("Lesson not found with ID: " + lessonId));

            String objectName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            try (InputStream inputStream = file.getInputStream()) {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucket)
                                .object(objectName)
                                .stream(inputStream, file.getSize(), -1)
                                .contentType(file.getContentType())
                                .build()
                );
            }

            File savedFile = File.builder()
                    .name(file.getOriginalFilename())
                    .url(objectName)
                    .lesson(lesson)
                    .createdTime(LocalDateTime.now())
                    .build();

            fileRepository.save(savedFile);

            return "File uploaded and saved to DB successfully!";
        } catch (Exception e) {
            log.error("Error during file upload", e);
            return "Error during file upload: " + e.getMessage();
        }
    }

    @Override
    public ByteArrayResource downloadFile(Long fileId) {
        try {
            File file = fileRepository.findById(fileId)
                    .orElseThrow(() -> new RuntimeException("File not found with ID: " + fileId));

            Lesson lesson = file.getLesson();
            if (!hasAccessToLesson(lesson)) {
                throw new RuntimeException("Access denied to this lesson");
            }

            try (InputStream stream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucket)
                            .object(file.getUrl())
                            .build())
            ) {
                byte[] byteArray = IOUtils.toByteArray(stream);
                return new ByteArrayResource(byteArray);
            }

        } catch (Exception e) {
            log.error("Download failed", e);
            throw new RuntimeException("Download failed: " + e.getMessage());
        }
    }

    @Override
    public List<FileResponse> getFileList() {
        return fileMapper.toDtoList(fileRepository.findAll());
    }

    private boolean hasAccessToLesson(Lesson lesson) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        boolean isAdminOrTeacher = auth.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN") || role.getAuthority().equals("ROLE_TEACHER"));

        if (isAdminOrTeacher) {
            return true;
        }

        return lesson.getChapter()
                .getCourse()
                .getUsers()
                .stream()
                .anyMatch(user -> user.getUsername().equals(username));
    }
}
