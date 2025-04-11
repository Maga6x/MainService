package kz.bitlab.mainservice.service;


import kz.bitlab.mainservice.dto.LessonRequest;
import kz.bitlab.mainservice.dto.LessonResponse;

import java.util.List;

public interface LessonService {
    List<LessonResponse> getLesson();

    LessonResponse getLessonById(Long id);

    void createLesson(LessonRequest request);

    void deleteLessonById(Long id);
}
