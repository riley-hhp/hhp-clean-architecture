package io.hhplus.tdd.infrastructure.lecture;

import io.hhplus.tdd.domain.constant.ErrorMessage;
import io.hhplus.tdd.domain.lecture.LectureRepository;
import io.hhplus.tdd.infrastructure.lecture.entity.Lecture;
import io.hhplus.tdd.infrastructure.lecture.entity.LectureItem;
import io.hhplus.tdd.infrastructure.lecture.entity.LectureSignUp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Supplier;

@Repository
@RequiredArgsConstructor
public class LectureRepositoryImpl implements LectureRepository {

    private final LectureJpaRepository lectureJpaRepository;
    private final LectureItemJpaRepository lectureItemJpaRepository;
    private final LectureSignUpJpaRepository lectureSignUpJpaRepository;

    Supplier<RuntimeException> LECTURE_EXCEPTION = () -> new RuntimeException(ErrorMessage.NOT_FOUND_ENTITY.format("Lecture"));
    Supplier<RuntimeException> LECTUER_ITEM_EXCEPTION = () -> new RuntimeException(ErrorMessage.NOT_FOUND_ENTITY.format("LectureItem"));
    Supplier<RuntimeException> LECTURE_SIGN_UP_EXCEPTION = () -> new RuntimeException(ErrorMessage.NOT_FOUND_ENTITY.format("LectureSignUp"));

    @Override
    public Lecture findLectureById(Long lectureId) {
        return lectureJpaRepository.findById(lectureId).orElseThrow(LECTURE_EXCEPTION);
    }

    @Override
    public LectureItem findLectureItemById(Long lectureItemId) {
        return lectureItemJpaRepository.findById(lectureItemId).orElseThrow(LECTUER_ITEM_EXCEPTION);
    }

    @Override
    public LectureSignUp findLectureSignUpById(Long signupId) {
        return lectureSignUpJpaRepository.findById(signupId).orElseThrow(LECTURE_SIGN_UP_EXCEPTION);
    }

    @Override
    public LectureSignUp findLectureSignUpByUserIdAndLectureItemId(Long userId, Long lectureItemId) {
        return lectureSignUpJpaRepository.findByUserIdAndLectureItemId(userId, lectureItemId).orElse(null);
    }

    public List<Lecture> findAllLecturesById(List<Long> lectureIds) {
        return lectureJpaRepository.findAllById(lectureIds).stream().toList();
    }

    public List<LectureItem> findAllLectureItemsById(List<Long> lectureItemIds) {
        return lectureItemJpaRepository.findAllById(lectureItemIds).stream().toList();
    }

    public List<LectureSignUp> findAllLectureSignUpByUserId(Long userId) {
        return lectureSignUpJpaRepository.findAllByUserId(userId).stream().toList();
    }

    public List<LectureSignUp> findAllLectureSignUpsById(List<Long> signupIds) {
        return lectureSignUpJpaRepository.findAllById(signupIds).stream().toList();
    }

    public List<LectureSignUp> findLectureSignUpByLectureItemId(Long lectureItemId) {
        return lectureSignUpJpaRepository.findAllByLectureItemId(lectureItemId).stream().toList();
    }

    public List<LectureItem> findAvailableLectureItems(LocalDateTime today) {
        return lectureItemJpaRepository.findAvailableLectureItems(today).stream().toList();
    }

    @Override
    public LectureItem findLectureItemForSignUp(Long lectureItemId) {
        return lectureItemJpaRepository.findLectureItemForSignUp(lectureItemId).orElseThrow(LECTUER_ITEM_EXCEPTION);
    }

    @Override
    public Long signupForLecture(Long lectureId, Long lectureItemId, Long userId) {
        return signupForLecture(new LectureSignUp(lectureId, lectureItemId, userId));
    }

    @Override
    public Long signupForLecture(LectureSignUp lectureSignUp) {
        return lectureSignUpJpaRepository.save(lectureSignUp).getSignupId();
    }

    @Override
    public void decreaseCapacityByLectureItem(Long lectureItemId) {
        LectureItem lectureItem = lectureItemJpaRepository.findLectureItemForSignUp(lectureItemId).orElseThrow(LECTUER_ITEM_EXCEPTION);
        lectureItem.decreaseCapacity();
        lectureItemJpaRepository.save(lectureItem);
    }
}
