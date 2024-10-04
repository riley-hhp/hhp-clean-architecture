package io.hhplus.tdd.domain.lecture;

import io.hhplus.tdd.infrastructure.lecture.entity.Lecture;
import io.hhplus.tdd.infrastructure.lecture.entity.LectureItem;
import io.hhplus.tdd.infrastructure.lecture.entity.LectureSignUp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 조회
 *
 */
@Service
@RequiredArgsConstructor
public class LectureQuery {

    private final LectureRepository lectureRepository;

    public LectureSignUp findLectureSignUpByUserIdAndLectureItemId(Long userId, Long lectureItemId) {
        return lectureRepository.findLectureSignUpByUserIdAndLectureItemId(userId, lectureItemId);
    }

    public List<LectureItem> findAvailableLectureItems(LocalDateTime today) {
        return lectureRepository.findAvailableLectureItems(today);
    }

    public List<LectureSignUp> findAllLectureSignUpByUserId(Long userId) {
        return lectureRepository.findAllLectureSignUpByUserId(userId);
    }

    public List<Lecture> findAllLecturesById(List<Long> lectureIds) {
        return lectureRepository.findAllLecturesById(lectureIds);
    }
}
