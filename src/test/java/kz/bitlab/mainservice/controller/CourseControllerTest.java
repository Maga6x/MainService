package kz.bitlab.mainservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.bitlab.mainservice.dto.CourseRequest;
import kz.bitlab.mainservice.dto.CourseResponse;
import kz.bitlab.mainservice.exception.EntityNotFoundException;
import kz.bitlab.mainservice.exception.EntityUniqueException;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CourseControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CourseService courseService;

    @InjectMocks
    private CourseController courseController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(courseController).build();
    }

    @Test
    void getCourse_success() throws Exception {
        CourseResponse response = new CourseResponse();
        response.setId(1L);
        response.setName("Java");
        when(courseService.getCourse(null, null)).thenReturn(List.of(response));

        mockMvc.perform(get("/course"))
                .andExpect(status().isOk());
    }

    @Test
    void getCourse_internalError() throws Exception {
        when(courseService.getCourse(null, null)).thenThrow(new RuntimeException());
        mockMvc.perform(get("/course"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getCourseById_success() throws Exception {
        CourseResponse response = new CourseResponse();
        response.setId(1L);
        response.setName("Java");
        when(courseService.getCourseById(1L)).thenReturn(response);

        mockMvc.perform(get("/course/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getCourseById_notFound() throws Exception {
        when(courseService.getCourseById(1L)).thenThrow(new EntityNotFoundException("Not found"));

        mockMvc.perform(get("/course/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createCourse_success() throws Exception {
        CourseRequest request = new CourseRequest();
        request.setName("Java");

        mockMvc.perform(post("/course")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(courseService, times(1)).createCourse(any(CourseRequest.class));
    }

    @Test
    void createCourse_duplicateName() throws Exception {
        doThrow(new EntityUniqueException("Duplicate")).when(courseService).createCourse(any());

        CourseRequest request = new CourseRequest();
        request.setName("Java");

        mockMvc.perform(post("/course")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void editCourse_success() throws Exception {
        CourseResponse response = new CourseResponse();
        response.setId(1L);
        response.setName("Java");

        mockMvc.perform(put("/course")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(response)))
                .andExpect(status().isOk());

        verify(courseService, times(1)).editCourse(any(CourseResponse.class));
    }

    @Test
    void deleteCourse_success() throws Exception {
        mockMvc.perform(delete("/course/1"))
                .andExpect(status().isNoContent());

        verify(courseService, times(1)).deleteCourseById(1L);
    }

    @Test
    void deleteCourse_notFound() throws Exception {
        doThrow(new EntityNotFoundException("Not found")).when(courseService).deleteCourseById(1L);

        mockMvc.perform(delete("/course/1"))
                .andExpect(status().isNotFound());
    }
}
