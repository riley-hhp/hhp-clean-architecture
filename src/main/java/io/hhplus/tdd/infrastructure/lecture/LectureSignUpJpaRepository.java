package io.hhplus.tdd.infrastructure.lecture;

import io.hhplus.tdd.infrastructure.lecture.entity.LectureSignUp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LectureSignUpJpaRepository extends JpaRepository<LectureSignUp, Long> {

    long countByUserId(long userId);
    List<LectureSignUp> findAllByUserId(Long userId);
    List<LectureSignUp> findAllByLectureItemId(Long LectureItemId);

    @Query("SELECT lsu FROM LectureSignUp lsu WHERE lsu.userId = :userId AND lsu.lectureItemId = :lectureItemId")
    Optional<LectureSignUp> findByUserIdAndLectureItemId(@Param("userId") Long userId, @Param("lectureItemId") Long lectureItemId);

}
