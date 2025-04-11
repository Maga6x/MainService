package kz.bitlab.mainservice.service;

import kz.bitlab.mainservice.dto.CourseRequest;
import kz.bitlab.mainservice.dto.CourseResponse;
import kz.bitlab.mainservice.dto.LessonRequest;
import kz.bitlab.mainservice.dto.LessonResponse;
import kz.bitlab.mainservice.entity.Course;
import kz.bitlab.mainservice.entity.Lesson;
import kz.bitlab.mainservice.exception.EntityNotFoundException;
import kz.bitlab.mainservice.exception.EntityUniqueException;
import kz.bitlab.mainservice.repository.CourseRepository;
import kz.bitlab.mainservice.repository.LessonRepository;
import kz.bitlab.mainservice.service.impl.CourseServiceImpl;
import kz.bitlab.mainservice.service.impl.LessonServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LessonServiceImplTest {

    @Mock
    private LessonRepository lessonRepository;

    @InjectMocks
    private LessonServiceImpl lessonService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getLesson_success() {
        Lesson lesson = new Lesson();
        lesson.setId(1L);
        lesson.setName("Java");

        when(lessonRepository.findAll()).thenReturn(List.of(lesson));

        List<LessonResponse> result = lessonService.getLesson();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Java", result.get(0).getName());
    }

    @Test
    void getLessonById_success() {
        Lesson lesson = new Lesson();
        lesson.setId(1L);
        lesson.setName("Java");

        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));

        LessonResponse response = lessonService.getLessonById(1L);

        assertNotNull(response);
        assertEquals("Java", response.getName());
    }

    @Test
    void getLessonById_notFound() {
        when(lessonRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> lessonService.getLessonById(1L));
    }

    @Test
    void createLesson_success() {
        LessonRequest request = new LessonRequest();
        request.setName("Java");

        when(lessonRepository.findByName("Java")).thenReturn(Optional.empty());

        lessonService.createLesson(request);

        verify(lessonRepository, times(1)).save(any(Lesson.class));
    }

    @Test
    void createLesson_duplicateName_throwsException() {
        LessonRequest request = new LessonRequest();
        request.setName("Java");

        Lesson existingLesson = new Lesson();
        existingLesson.setId(1L);
        existingLesson.setName("Java");

        when(lessonRepository.findByName("Java")).thenReturn(Optional.of(existingLesson));

        assertThrows(EntityUniqueException.class, () -> lessonService.createLesson(request));
    }

    @Test
    void deleteLessonById_success() {
        Lesson lesson = new Lesson();
        lesson.setId(1L);
        lesson.setName("Java");

        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));

        lessonService.deleteLessonById(1L);

        verify(lessonRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteLessonById_notFound() {
        when(lessonRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> lessonService.deleteLessonById(1L));
    }
}
