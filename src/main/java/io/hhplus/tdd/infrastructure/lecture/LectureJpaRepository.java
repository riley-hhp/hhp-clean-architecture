package io.hhplus.tdd.infrastructure.lecture;

import io.hhplus.tdd.infrastructure.lecture.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureJpaRepository extends JpaRepository<Lecture, Long> {}
