package kz.bitlab.mainservice.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import kz.bitlab.mainservice.service.CourseEnglishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/courses")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "CourseEnglishController", description = "API для управления с курсами")
public class CourseEnglishController {

    private final CourseEnglishService courseEnglishService;


}
