package kz.bitlab.mainservice.service;

import kz.bitlab.mainservice.dto.ChapterRequest;
import kz.bitlab.mainservice.dto.ChapterResponse;
import kz.bitlab.mainservice.entity.Chapter;
import kz.bitlab.mainservice.exception.EntityNotFoundException;
import kz.bitlab.mainservice.exception.EntityUniqueException;
import kz.bitlab.mainservice.repository.ChapterRepository;
import kz.bitlab.mainservice.service.impl.ChapterServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChapterServiceImplTest {

    @Mock
    private ChapterRepository chapterRepository;

    @InjectMocks
    private ChapterServiceImpl chapterService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getChapter_success() {
        Chapter chapter = new Chapter();
        chapter.setId(1L);
        chapter.setName("Chapter 1");

        when(chapterRepository.findAll()).thenReturn(List.of(chapter));

        List<ChapterResponse> responses = chapterService.getChapter();

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("Chapter 1", responses.get(0).getName());
    }

    @Test
    void getChapterById_success() {
        Chapter chapter = new Chapter();
        chapter.setId(1L);
        chapter.setName("Chapter 1");

        when(chapterRepository.findById(1L)).thenReturn(Optional.of(chapter));

        ChapterResponse response = chapterService.getChapterById(1L);
        assertNotNull(response);
        assertEquals("Chapter 1", response.getName());
    }

    @Test
    void getChapterById_notFound() {
        when(chapterRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> chapterService.getChapterById(1L));
    }

    @Test
    void createChapter_success() {
        ChapterRequest request = new ChapterRequest();
        request.setName("New Chapter");

        when(chapterRepository.findByName("New Chapter")).thenReturn(Optional.empty());

        chapterService.createChapter(request);
        verify(chapterRepository, times(1)).save(any(Chapter.class));
    }

    @Test
    void createChapter_duplicateName() {
        ChapterRequest request = new ChapterRequest();
        request.setName("Duplicate");

        Chapter existing = new Chapter();
        existing.setId(2L);
        existing.setName("Duplicate");

        when(chapterRepository.findByName("Duplicate")).thenReturn(Optional.of(existing));

        assertThrows(EntityUniqueException.class, () -> chapterService.createChapter(request));
    }

    @Test
    void deleteChapterById_success() {
        Chapter chapter = new Chapter();
        chapter.setId(1L);
        chapter.setName("Chapter 1");

        when(chapterRepository.findById(1L)).thenReturn(Optional.of(chapter));

        chapterService.deleteChapterById(1L);
        verify(chapterRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteChapterById_notFound() {
        when(chapterRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> chapterService.deleteChapterById(1L));
    }
}
