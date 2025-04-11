package kz.bitlab.mainservice.service.impl;

import jakarta.persistence.EntityNotFoundException;
import kz.bitlab.mainservice.dto.LessonRequest;
import kz.bitlab.mainservice.dto.LessonResponse;
import kz.bitlab.mainservice.entity.Lesson;
import kz.bitlab.mainservice.exception.EntityUniqueException;
import kz.bitlab.mainservice.mapper.LessonMapper;
import kz.bitlab.mainservice.repository.LessonRepository;
import kz.bitlab.mainservice.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;

    @Override
    public List<LessonResponse> getLesson() {
        List<Lesson> lessons = lessonRepository.findAll();
        return LessonMapper.INSTANCE.toDtoList(lessons);
    }

    @Override
    public LessonResponse getLessonById(Long id) {
        return lessonRepository.findById(id)
                .map(lesson -> LessonMapper.INSTANCE.toDto(lesson))
                .orElseThrow(() -> new EntityNotFoundException("Ingredient not found"));
    }

    private void checkNameUnique(String name, Long lessonId) {
        lessonRepository.findByName(name)
                .filter(entity -> Objects.equals(entity.getId(), lessonId))
                .ifPresent(lesson -> {
                    throw new EntityUniqueException("Recipe already exists");
                });
    }

    @Override
    public void createLesson(LessonRequest request) {
        checkNameUnique(request.getName(), null);
        Lesson lesson = LessonMapper.INSTANCE.toEntity(request);
        lessonRepository.save(lesson);
    }

    @Override
    public void deleteLessonById(Long id) {
        getLessonById(id);
        lessonRepository.deleteById(id);
    }
}
