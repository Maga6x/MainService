package kz.bitlab.mainservice.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import kz.bitlab.mainservice.dto.CourseResponse;
import kz.bitlab.mainservice.entity.Course;
import kz.bitlab.mainservice.exception.EntityUniqueException;
import kz.bitlab.mainservice.mapper.CourseMapper;
import kz.bitlab.mainservice.repository.CourseRepository;
import kz.bitlab.mainservice.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
