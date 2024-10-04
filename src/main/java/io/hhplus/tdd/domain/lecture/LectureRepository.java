package io.hhplus.tdd.domain.lecture;

import io.hhplus.tdd.infrastructure.lecture.entity.Lecture;
import io.hhplus.tdd.infrastructure.lecture.entity.LectureItem;
import io.hhplus.tdd.infrastructure.lecture.entity.LectureSignUp;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LectureRepository {

    Lecture findLectureById(Long lectureId);

    LectureItem findLectureItemById(Long lectureItemId);

    LectureSignUp findLectureSignUpById(Long signupId);

    LectureSignUp findLectureSignUpByUserIdAndLectureItemId(Long userId, Long lectureItemId);

    List<LectureItem> findAvailableLectureItems(LocalDateTime today);

    List<LectureSignUp> findAllLectureSignUpByUserId(Long userId);

    List<Lecture> findAllLecturesById(List<Long> lectureIds);

    LectureItem findLectureItemForSignUp(Long lectureItemId);

    Long signupForLecture(LectureSignUp lectureSignUp);

    Long signupForLecture(Long lectureId, Long lectureItemId, Long userId);

    //    TODO) STEP3
//    void decreaseCapacityByLectureItem(Long lectureItemId);
}
