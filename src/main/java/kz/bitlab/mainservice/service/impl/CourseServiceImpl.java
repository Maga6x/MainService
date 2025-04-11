package kz.bitlab.mainservice.service.impl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import kz.bitlab.mainservice.dto.CourseRequest;
import kz.bitlab.mainservice.dto.CourseResponse;
import kz.bitlab.mainservice.entity.Course;
import kz.bitlab.mainservice.exception.EntityUniqueException;
import kz.bitlab.mainservice.mapper.CourseMapper;
import kz.bitlab.mainservice.repository.CourseRepository;
import kz.bitlab.mainservice.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<CourseResponse> getCourse(String name, String description) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Course> cq = cb.createQuery(Course.class);
        Root<Course> root = cq.from(Course.class);

        List<Predicate> predicates = new ArrayList<>();

        if (name != null && !name.isEmpty()) {
            predicates.add(cb.equal(cb.lower(root.get("name")), name.toLowerCase()));
        }

        if (description != null && !description.isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("description")), "%"+description.toLowerCase()+"%"));
        }
        List<Course> course = entityManager.createQuery(cq).getResultList();
        return CourseMapper.INSTANCE.toDtoList(course);
    }

    @Override
    public CourseResponse getCourseById(Long id) {
        return courseRepository.findById(id)
                .map(course -> CourseMapper.INSTANCE.toDto(course))
                .orElseThrow(()-> new EntityNotFoundException("Recipe not found"));

    }
    private void checkNameUnique(String name, Long courseId) {
        courseRepository.findByName(name)
                .filter(entity -> Objects.equals(entity.getId(), courseId))
                .ifPresent(course -> {
                    throw new EntityUniqueException("Recipe already exists");
                });
    }

    @Override
    public void createCourse(CourseRequest request) {
        checkNameUnique(request.getName(), null);
        Course course = CourseMapper.INSTANCE.toEntity(request);
        courseRepository.save(course);
    }

    @Override
    public void editCourse(CourseResponse response) {
        getCourseById(response.getId());
        checkNameUnique(response.getName(), response.getId());

        Course course = CourseMapper.INSTANCE.toEntity(response);
        courseRepository.save(course);
    }

    @Override
    public void deleteCourseById(Long id) {
        getCourseById(id);
        courseRepository.deleteById(id);
    }


}
