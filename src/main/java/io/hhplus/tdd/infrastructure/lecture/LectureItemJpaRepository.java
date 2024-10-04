package io.hhplus.tdd.infrastructure.lecture;

import io.hhplus.tdd.infrastructure.lecture.entity.LectureItem;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LectureItemJpaRepository extends JpaRepository<LectureItem, Long> {

    @Query("SELECT li FROM LectureItem li WHERE li.capacity > 0 AND li.lectureDateTime > :today")
    List<LectureItem> findAvailableLectureItems(@Param("today") LocalDateTime today);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT li FROM LectureItem li WHERE li.id = :lectureItemId")
    Optional<LectureItem> findLectureItemForSignUp(@Param("lectureItemId") Long lectureItemId);

}
