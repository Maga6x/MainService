package kz.bitlab.mainservice.service;
import kz.bitlab.mainservice.dto.CourseRequest;
import kz.bitlab.mainservice.dto.CourseResponse;
import kz.bitlab.mainservice.entity.Course;
import kz.bitlab.mainservice.exception.EntityUniqueException;
import kz.bitlab.mainservice.repository.CourseRepository;
import kz.bitlab.mainservice.service.impl.CourseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseServiceImplTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseServiceImpl courseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCourseById_success() {
        Course course = new Course();
        course.setId(1L);
        course.setName("Java");

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        CourseResponse response = courseService.getCourseById(1L);
        assertNotNull(response);
        assertEquals("Java", response.getName());
    }

    @Test
    void getCourseById_notFound() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(jakarta.persistence.EntityNotFoundException.class, () -> courseService.getCourseById(1L));
    }

    @Test
    void createCourse_success() {
        CourseRequest request = new CourseRequest();
        request.setName("Java");

        when(courseRepository.findByName("Java")).thenReturn(Optional.empty());

        courseService.createCourse(request);
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void createCourse_duplicateName_throwsException() {
        CourseRequest request = new CourseRequest();
        request.setName("Java");

        Course existingCourse = new Course();
        existingCourse.setId(1L);
        existingCourse.setName("Java");

        when(courseRepository.findByName("Java")).thenReturn(Optional.of(existingCourse));

        assertThrows(EntityUniqueException.class, () -> courseService.createCourse(request));
    }

    @Test
    void editCourse_success() {
        CourseResponse response = new CourseResponse();
        response.setId(1L);
        response.setName("Java");

        Course course = new Course();
        course.setId(1L);
        course.setName("Java");

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseRepository.findByName("Java")).thenReturn(Optional.empty());

        courseService.editCourse(response);
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void deleteCourseById_success() {
        Course course = new Course();
        course.setId(1L);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        courseService.deleteCourseById(1L);
        verify(courseRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteCourseById_notFound() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(jakarta.persistence.EntityNotFoundException.class, () -> courseService.deleteCourseById(1L));
    }
}
