package kz.bitlab.mainservice.service;


import kz.bitlab.mainservice.dto.CourseRequest;
import kz.bitlab.mainservice.dto.CourseResponse;

import java.util.List;

public interface CourseService {

    List<CourseResponse> getCourse(String name, String description);

    CourseResponse getCourseById(Long id);

    void createCourse(CourseRequest request);

    void editCourse(CourseResponse response);

    void deleteCourseById(Long id);
}
