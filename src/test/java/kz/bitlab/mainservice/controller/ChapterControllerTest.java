package kz.bitlab.mainservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.bitlab.mainservice.dto.ChapterRequest;
import kz.bitlab.mainservice.dto.ChapterResponse;
import kz.bitlab.mainservice.dto.CourseRequest;
import kz.bitlab.mainservice.dto.CourseResponse;
import kz.bitlab.mainservice.exception.EntityNotFoundException;
import kz.bitlab.mainservice.exception.EntityUniqueException;
import kz.bitlab.mainservice.service.ChapterService;
import kz.bitlab.mainservice.service.CourseService;
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

class ChapterControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ChapterService chapterService;

    @InjectMocks
    private ChapterController chapterController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(chapterController).build();
    }

    @Test
    void getChapters_success() throws Exception {
        ChapterResponse response = new ChapterResponse();
        response.setId(1L);
        response.setName("Chapter 1");
        when(chapterService.getChapter()).thenReturn(List.of(response));

        mockMvc.perform(get("/chapter"))
                .andExpect(status().isOk());
    }

    @Test
    void getChapters_internalError() throws Exception {
        when(chapterService.getChapter()).thenThrow(new RuntimeException());

        mockMvc.perform(get("/chapter"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getChapterById_success() throws Exception {
        ChapterResponse response = new ChapterResponse();
        response.setId(1L);
        response.setName("Chapter 1");
        when(chapterService.getChapterById(1L)).thenReturn(response);

        mockMvc.perform(get("/chapter/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getChapterById_notFound() throws Exception {
        when(chapterService.getChapterById(1L)).thenThrow(new EntityNotFoundException("Not found"));

        mockMvc.perform(get("/chapter/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createChapter_success() throws Exception {
        ChapterRequest request = new ChapterRequest();
        request.setName("New Chapter");

        mockMvc.perform(post("/chapter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(chapterService, times(1)).createChapter(any(ChapterRequest.class));
    }

    @Test
    void createChapter_duplicate() throws Exception {
        doThrow(new EntityUniqueException("Already exists")).when(chapterService).createChapter(any());

        ChapterRequest request = new ChapterRequest();
        request.setName("Duplicate");

        mockMvc.perform(post("/chapter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteChapter_success() throws Exception {
        mockMvc.perform(delete("/chapter/1"))
                .andExpect(status().isNoContent());

        verify(chapterService, times(1)).deleteChapterById(1L);
    }

    @Test
    void deleteChapter_notFound() throws Exception {
        doThrow(new EntityNotFoundException("Not found")).when(chapterService).deleteChapterById(1L);

        mockMvc.perform(delete("/chapter/1"))
                .andExpect(status().isNotFound());
    }
}
