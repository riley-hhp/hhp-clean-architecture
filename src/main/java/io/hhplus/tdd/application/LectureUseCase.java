package io.hhplus.tdd.application;

import io.hhplus.tdd.domain.constant.ErrorMessage;
import io.hhplus.tdd.domain.lecture.LectureCommand;
import io.hhplus.tdd.domain.lecture.LectureQuery;
import io.hhplus.tdd.domain.user.UserQuery;
import io.hhplus.tdd.infrastructure.lecture.entity.Lecture;
import io.hhplus.tdd.infrastructure.lecture.entity.LectureItem;
import io.hhplus.tdd.infrastructure.lecture.entity.LectureSignUp;
import io.hhplus.tdd.infrastructure.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LectureUseCase {

    private static final Logger log = LoggerFactory.getLogger(LectureUseCase.class);

    private final LectureQuery lectureQuery;
    private final LectureCommand lectureCommand;
    private final UserQuery userQuery;

    /**
     * 1. 신청가능목록조회
     * capacity > 0
     * lectureDateTime > today
     * 기존에 신청하지 않은 강좌
     *
     */
    public List<LectureItem> findAvailableLectureItems(Long userId) {

        User user = userQuery.findByUserId(userId);
        List<LectureItem> availableItems = lectureQuery.findAvailableLectureItems(LocalDateTime.now());
        List<Long> signedLectureIds = lectureQuery.findAllLectureSignUpByUserId(userId).stream().map(LectureSignUp::getLectureId).distinct().toList();
        return availableItems.stream()
                             .filter(lectureItem -> !signedLectureIds.contains(lectureItem.getLectureId()))
                             .toList();
    }

    /**
     * 2. 수강신청
     * 사용자 검증
     * 기 신청 강좌 확인
     * 신청가능한 강좌 확인(현재 날짜 이전의 강좌는 등록불가능)
     * 강의정원 확인
     * 신청
     * 강의 정원 차감
     *
     * @return
     */
    @Transactional
    public boolean signUpForLecture(Long lectureId, Long lectureItemId, Long userId) {

        //사용자 확인
        User user = userQuery.findByUserId(userId);

        //TODO STEP4 동일한 특강 신청하지 못하도록
        // (커밋이 섞여서 부득이하게 todo로 표기합니다)
        //기수강이력 확인
        LectureSignUp exist = lectureQuery.findLectureSignUpByUserIdAndLectureItemId(userId, lectureItemId);
        if ( exist != null ) {
            throw new RuntimeException(ErrorMessage.ALREADY_SIGNED_LECTURE.getMessage());
        }
        //STEP4 동일한 특강 신청하지 못하도록

        //강좌 아이템 정보 확인( 날짜, 정원 )
        LectureItem lectureItem = lectureCommand.findLectureItemForSignUp(lectureItemId);
        log.debug("lectureItem.getCapacity() = {}", lectureItem.getCapacity());
        if ( lectureItem.getLectureDateTime().isEqual(LocalDateTime.now())||lectureItem.getLectureDateTime().isBefore(LocalDateTime.now()) ) {
            log.debug("LocalDateTime.now() = {}", LocalDateTime.now());
            log.debug("lectureItem.getLectureDateTime() = {}", lectureItem.getLectureDateTime());
            throw new RuntimeException(ErrorMessage.INVALID_DATE.getMessage());
        }
        if ( lectureItem.getCapacity() <= 0 ) {
            throw new RuntimeException(ErrorMessage.INSUFFICIENT_CAPACITY.getMessage());
        }

        //신청
        Long singUpId = lectureCommand.signUpForLecture(new LectureSignUp(lectureId, lectureItemId, userId));
        if (singUpId > 0) {
            lectureCommand.decreaseCapacity(lectureItemId); //정원차감
            return true;
        }
        return false;
    }


    /**
     * 3 신청완료목록조회
     *
     */
    public List<Lecture> findAllLecturesByUserId(Long userId) {

        User user = userQuery.findByUserId(userId);
        List<Long> lectureIds = lectureQuery.findAllLectureSignUpByUserId(userId)
                                            .stream()
                                            .map(LectureSignUp::getLectureId)
                                            .distinct()
                                            .toList();

        return lectureQuery.findAllLecturesById(lectureIds);
    }

}
