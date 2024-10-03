package io.hhplus.tdd.domain.lecture;

import io.hhplus.tdd.application.LectureUseCase;
import io.hhplus.tdd.infrastructure.lecture.entity.LectureItem;
import io.hhplus.tdd.infrastructure.lecture.entity.LectureSignUp;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 등록
 *
 */
@Service
@RequiredArgsConstructor
public class LectureCommand {

    private final LectureRepository lectureRepository;
    private static final Logger log = LoggerFactory.getLogger(LectureCommand.class);

    public LectureItem findLectureItemForSignUp(Long lectureItemId) {
        log.debug("비관적 락 실행");
        return lectureRepository.findLectureItemForSignUp(lectureItemId);
    }

    public Long signUpForLecture(LectureSignUp signUp) {

        return lectureRepository.signupForLecture(signUp);
    }

    //    TODO) STEP3
//    public void decreaseCapacity(Long lectureItemId){
//
//        lectureRepository.decreaseCapacityByLectureItem(lectureItemId);
//    }
}
