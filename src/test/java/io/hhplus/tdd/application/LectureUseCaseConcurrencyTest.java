package io.hhplus.tdd.application;

import io.hhplus.tdd.domain.constant.ErrorMessage;
import io.hhplus.tdd.infrastructure.lecture.LectureItemJpaRepository;
import io.hhplus.tdd.infrastructure.lecture.LectureJpaRepository;
import io.hhplus.tdd.infrastructure.lecture.LectureSignUpJpaRepository;
import io.hhplus.tdd.infrastructure.lecture.entity.Lecture;
import io.hhplus.tdd.infrastructure.lecture.entity.LectureItem;
import io.hhplus.tdd.infrastructure.user.UserJpaRepository;
import io.hhplus.tdd.infrastructure.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DisplayName("LectureUseCase 동시성 테스트")
class LectureUseCaseConcurrencyTest {

    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private LectureJpaRepository lectureJpaRepository;
    @Autowired
    private LectureItemJpaRepository lectureItemJpaRepository;
    @Autowired
    private LectureSignUpJpaRepository lectureSignUpJpaRepository;
    @Autowired
    private LectureUseCase useCase;

    @BeforeEach
    void setUp() {

        lectureJpaRepository.deleteAll();
        lectureItemJpaRepository.deleteAll();
        lectureSignUpJpaRepository.deleteAll();

        // Test setup
        long userId = 1L;
        for (int i = 1; i <= 50; i++) {

            User userEntity = userJpaRepository.save(new User(userId+i,"test"+i));
            System.out.println("userEntity = " + userEntity);
        }


        Lecture lecture = lectureJpaRepository.save(new Lecture("강좌1", "코치1"));
        lectureItemJpaRepository.save(new LectureItem(lecture.getLectureId(), LocalDateTime.now().plusDays(1)));
    }

    @Test
    @DisplayName("동시성 테스트: 여러 사용자가 동시에 강의 신청")
    void testConcurrentSignUpForLecture() throws InterruptedException {

        int numberOfThreads = 30; // 시뮬레이션할 스레드 수
        CountDownLatch latch = new CountDownLatch(numberOfThreads); // 동기화 도구
        List<Thread> threads = new ArrayList<>();

        for (int i = 1; i <= numberOfThreads; i++) {
            final long userId = i + 1; // 각 스레드에 다른 사용자 ID 할당
            Thread thread = new Thread(() -> {
                try {
                    useCase.signUpForLecture(1L, 1L, userId); // 강의 신청
                }
                catch (RuntimeException e) {
                    // 예외 처리 (예: 정원이 부족한 경우)
                    System.out.println("User " + userId + " failed to sign up: " + e.getMessage());
                }
                finally {
                    latch.countDown(); // 스레드 종료 시 카운트 감소
                }
            });
            threads.add(thread);
            thread.start();
        }

        latch.await(); // 모든 스레드가 종료될 때까지 대기

        // 결과 검증: 강의 신청 후 남은 좌석 수를 확인
        LectureItem lectureItem = lectureItemJpaRepository.findById(1L).orElseThrow();
        assertEquals(0, lectureItem.getCapacity(), "정원이 모두 차감되어야 합니다.");
    }

    @Test
    @DisplayName("동시성 테스트: 40명이 동시에 신청할 때 30명만 성공")
    void testConcurrentSignUpForLectureWithCapacity() throws InterruptedException {

        int numberOfThreads = 40; // 스레드 수
        CountDownLatch latch = new CountDownLatch(numberOfThreads); // 동기화 도구
        List<Long> successfulUsers = new ArrayList<>();

        for (int i = 1; i <= numberOfThreads; i++) {
            final long userId = i + 1; // 각 스레드에 다른 사용자 ID 할당
            Thread thread = new Thread(() -> {
                try {
                    boolean isSuccess = useCase.signUpForLecture(1L, 1L, userId); // 강의 신청
                    if (isSuccess) {
                        synchronized (successfulUsers) {
                            successfulUsers.add(userId);
                        }
                    }
                } catch (RuntimeException e) {
                    // 예외 처리 (예: 정원이 부족한 경우)
                    System.out.println("User " + userId + " failed to sign up: " + e.getMessage());
                } finally {
                    latch.countDown(); // 스레드 종료 시 카운트 감소
                }
            });
            thread.start();
        }

        latch.await(); // 모든 스레드가 종료될 때까지 대기

        // 결과 검증: 성공한 사용자의 수가 30명인지 확인
        assertEquals(30, successfulUsers.size(), "정원 초과로 인해 성공한 신청자는 30명이어야 합니다.");
    }

    @Test
    @DisplayName("동일한 사용자로 동일 특강에 여러 번 신청 시 1번만 성공")
    void testMultipleSignUpSameUser() throws InterruptedException {

        int numberOfAttempts = 5; // 동일 사용자로 신청 시도 횟수
        long userId = 2L; // 동일 사용자 ID
        try {
            // 여러 번 신청
            for (int i = 0; i < numberOfAttempts; i++) {
                useCase.signUpForLecture(1L, 1L, userId);
            }
        }
        catch (RuntimeException e) {
            System.out.println("User " + userId + " failed to sign up: " + e.getMessage());
            assertEquals(ErrorMessage.ALREADY_SIGNED_LECTURE.getMessage(), e.getMessage());
        }
        finally {
            // 결과 검증: 해당 사용자로 신청 성공한 기록 수 확인
            long successfulSignUps = lectureSignUpJpaRepository.countByUserId(userId);
            assertEquals(1, successfulSignUps, "동일 사용자는 1번만 신청 성공해야 합니다.");

        }
    }
}