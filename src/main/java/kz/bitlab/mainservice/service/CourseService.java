package kz.bitlab.mainservice.service;


import kz.bitlab.mainservice.dto.CourseResponse;

import java.util.List;

public interface CourseService {

    List<CourseResponse> getCourse(String name, String description);

    CourseResponse getCourseById(Long id);
}
