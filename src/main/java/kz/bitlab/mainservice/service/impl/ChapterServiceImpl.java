package kz.bitlab.mainservice.service.impl;

import jakarta.persistence.EntityNotFoundException;
import kz.bitlab.mainservice.dto.ChapterRequest;
import kz.bitlab.mainservice.dto.ChapterResponse;
import kz.bitlab.mainservice.entity.Chapter;
import kz.bitlab.mainservice.exception.EntityUniqueException;
import kz.bitlab.mainservice.mapper.ChapterMapper;
import kz.bitlab.mainservice.repository.ChapterRepository;
import kz.bitlab.mainservice.service.ChapterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ChapterServiceImpl implements ChapterService {

    private final ChapterRepository chapterRepository;

    @Override
    public List<ChapterResponse> getChapter() {
        List<Chapter> chapter = chapterRepository.findAll();
        return ChapterMapper.INSTANCE.toDtoList(chapter);
    }

    @Override
    public ChapterResponse getChapterById(Long id) {
        return chapterRepository.findById(id)
                .map(chapter -> ChapterMapper.INSTANCE.toDto(chapter))
                .orElseThrow(() -> new EntityNotFoundException("Ingredient not found"));
    }
    private void checkNameUnique(String name, Long chapterId) {
        chapterRepository.findByName(name)
                .filter(entity -> Objects.equals(entity.getId(), chapterId))
                .ifPresent(chapter -> {
                    throw new EntityUniqueException("Chapter already exists");
                });
    }

    @Override
    public void createChapter(ChapterRequest request) {
        checkNameUnique(request.getName(), null);
        Chapter chapter = ChapterMapper.INSTANCE.toEntity(request);
        chapterRepository.save(chapter);
    }

    @Override
    public void deleteChapterById(Long id) {
        getChapterById(id);
        chapterRepository.deleteById(id);
    }


}
