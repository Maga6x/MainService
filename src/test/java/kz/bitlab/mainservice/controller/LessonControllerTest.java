package kz.bitlab.mainservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.bitlab.mainservice.dto.LessonRequest;
import kz.bitlab.mainservice.dto.LessonResponse;
import kz.bitlab.mainservice.exception.EntityNotFoundException;
import kz.bitlab.mainservice.exception.EntityUniqueException;
import kz.bitlab.mainservice.service.LessonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LessonControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LessonService lessonService;

    @InjectMocks
    private LessonController lessonController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(lessonController).build();
    }

    @Test
    void getLessons_success() throws Exception {
        LessonResponse response = new LessonResponse();
        response.setId(1L);
        response.setName("Intro to Java");

        when(lessonService.getLesson()).thenReturn(List.of(response));

        mockMvc.perform(get("/lesson"))
                .andExpect(status().isOk());
    }

    @Test
    void getLessons_internalError() throws Exception {
        when(lessonService.getLesson()).thenThrow(new RuntimeException());

        mockMvc.perform(get("/lesson"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getLessonById_success() throws Exception {
        LessonResponse response = new LessonResponse();
        response.setId(1L);
        response.setName("Intro to Java");

        when(lessonService.getLessonById(1L)).thenReturn(response);

        mockMvc.perform(get("/lesson/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getLessonById_notFound() throws Exception {
        when(lessonService.getLessonById(1L)).thenThrow(new EntityNotFoundException("Not found"));

        mockMvc.perform(get("/lesson/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createLesson_success() throws Exception {
        LessonRequest request = new LessonRequest();
        request.setName("Java Basics");

        mockMvc.perform(post("/lesson")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(lessonService, times(1)).createLesson(any(LessonRequest.class));
    }

    @Test
    void createLesson_duplicateName() throws Exception {
        doThrow(new EntityUniqueException("Duplicate")).when(lessonService).createLesson(any());

        LessonRequest request = new LessonRequest();
        request.setName("Java Basics");

        mockMvc.perform(post("/lesson")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteLesson_success() throws Exception {
        mockMvc.perform(delete("/lesson/1"))
                .andExpect(status().isNoContent());

        verify(lessonService, times(1)).deleteLessonById(1L);
    }

    @Test
    void deleteLesson_notFound() throws Exception {
        doThrow(new EntityNotFoundException("Not found")).when(lessonService).deleteLessonById(1L);

        mockMvc.perform(delete("/lesson/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteLesson_internalError() throws Exception {
        doThrow(new RuntimeException()).when(lessonService).deleteLessonById(1L);

        mockMvc.perform(delete("/lesson/1"))
                .andExpect(status().isInternalServerError());
    }
}
