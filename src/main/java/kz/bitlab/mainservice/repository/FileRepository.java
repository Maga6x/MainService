package kz.bitlab.mainservice.repository;

import kz.bitlab.mainservice.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findAllByLessonId(Long lessonId);
}
