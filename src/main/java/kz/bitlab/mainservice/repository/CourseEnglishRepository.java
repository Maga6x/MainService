package kz.bitlab.mainservice.repository;

import kz.bitlab.mainservice.entity.CourseEnglish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseEnglishRepository extends JpaRepository<CourseEnglish, Long> {
}
