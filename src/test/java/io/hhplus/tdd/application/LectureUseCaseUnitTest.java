package io.hhplus.tdd.application;

import io.hhplus.tdd.domain.constant.ErrorMessage;
import io.hhplus.tdd.domain.lecture.LectureCommand;
import io.hhplus.tdd.domain.lecture.LectureQuery;
import io.hhplus.tdd.domain.user.UserQuery;
import io.hhplus.tdd.infrastructure.lecture.entity.Lecture;
import io.hhplus.tdd.infrastructure.lecture.entity.LectureItem;
import io.hhplus.tdd.infrastructure.lecture.entity.LectureSignUp;
import io.hhplus.tdd.infrastructure.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("LectureUseCase 단위 테스트")
class LectureUseCaseUnitTest {

    @Mock
    private LectureQuery lectureQuery;

    @Mock
    private LectureCommand lectureCommand;

    @Mock
    private UserQuery userQuery;

    @InjectMocks
    private LectureUseCase lectureUseCase;

    @Test
    @DisplayName("사용자가 신청 가능한 강의를 정상적으로 조회하는 경우")
    void findAvailableLectureItems_success() {
        // Given
        Long userId = 1L;
        User user = new User();
        List<LectureItem> availableItems = Arrays.asList(new LectureItem(), new LectureItem());
        List<LectureSignUp> signUps = Arrays.asList(new LectureSignUp(1L, 1L, userId));

        when(userQuery.findByUserId(userId)).thenReturn(user);
        when(lectureQuery.findAvailableLectureItems(any(LocalDateTime.class))).thenReturn(availableItems);
        when(lectureQuery.findAllLectureSignUpByUserId(userId)).thenReturn(signUps);

        // When
        List<LectureItem> result = lectureUseCase.findAvailableLectureItems(userId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(lectureQuery, times(1)).findAvailableLectureItems(any(LocalDateTime.class));
    }

    @Test
    @DisplayName("사용자가 존재하지 않는 경우")
    void signUpForLecture_userNotFound() {
        // Given
        Long lectureId = 1L, lectureItemId = 1L, userId = 1L;

        when(userQuery.findByUserId(userId)).thenThrow(new RuntimeException(ErrorMessage.INVALID_USER.getMessage()));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            lectureUseCase.signUpForLecture(lectureId, lectureItemId, userId);
        });
        assertEquals(ErrorMessage.INVALID_USER.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("사용자가 이미 해당 강의에 신청한 경우")
    void signUpForLecture_alreadySignedUp() {
        // Given
        Long lectureId = 1L, lectureItemId = 1L, userId = 1L;
        User user = new User();
        LectureSignUp existingSignUp = new LectureSignUp();

        when(userQuery.findByUserId(userId)).thenReturn(user);
        when(lectureQuery.findLectureSignUpByUserIdAndLectureItemId(userId, lectureItemId)).thenReturn(existingSignUp);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            lectureUseCase.signUpForLecture(lectureId, lectureItemId, userId);
        });
        assertEquals(ErrorMessage.ALREADY_SIGNED_LECTURE.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("강의가 이미 종료되었거나 신청 가능한 날짜가 아닌 경우")
    void signUpForLecture_invalidLectureDate() {
        // Given
        Long lectureId = 1L, lectureItemId = 1L, userId = 1L;
        User user = new User();
        LectureItem lectureItem = new LectureItem();
        lectureItem.setLectureDateTime(LocalDateTime.now().minusDays(1)); // 과거 날짜

        when(userQuery.findByUserId(userId)).thenReturn(user);
        when(lectureQuery.findLectureSignUpByUserIdAndLectureItemId(userId, lectureItemId)).thenReturn(null);
        when(lectureCommand.findLectureItemForSignUp(lectureItemId)).thenReturn(lectureItem);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            lectureUseCase.signUpForLecture(lectureId, lectureItemId, userId);
        });
        assertEquals(ErrorMessage.INVALID_DATE.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("강의 정원이 부족한 경우")
    void signUpForLecture_insufficientCapacity() {
        // Given
        Long lectureId = 1L, lectureItemId = 1L, userId = 1L;
        User user = new User();
        LectureItem lectureItem = new LectureItem();
        lectureItem.setLectureDateTime(LocalDateTime.now().plusDays(1)); // 미래 날짜
        lectureItem.setCapacity(0); // 정원이 0

        when(userQuery.findByUserId(userId)).thenReturn(user);
        when(lectureQuery.findLectureSignUpByUserIdAndLectureItemId(userId, lectureItemId)).thenReturn(null);
        when(lectureCommand.findLectureItemForSignUp(lectureItemId)).thenReturn(lectureItem);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            lectureUseCase.signUpForLecture(lectureId, lectureItemId, userId);
        });
        assertEquals(ErrorMessage.INSUFFICIENT_CAPACITY.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("모든 조건이 맞을 때 정상적으로 강의에 신청하고 정원을 차감하는 경우")
    void signUpForLecture_success() {
        // Given
        Long lectureId = 1L, lectureItemId = 1L, userId = 1L;
        User user = new User();
        LectureItem lectureItem = new LectureItem();
        lectureItem.setLectureDateTime(LocalDateTime.now().plusDays(1)); // 미래 날짜
        lectureItem.setCapacity(10); // 정원이 10
        LectureSignUp newSignUp = new LectureSignUp(lectureId, lectureItemId, userId);
        Long signUpId = 1L;

        when(userQuery.findByUserId(userId)).thenReturn(user);
        when(lectureQuery.findLectureSignUpByUserIdAndLectureItemId(userId, lectureItemId)).thenReturn(null);
        when(lectureCommand.findLectureItemForSignUp(lectureItemId)).thenReturn(lectureItem);
        when(lectureCommand.signUpForLecture(any(LectureSignUp.class))).thenReturn(signUpId);

        // When
        lectureUseCase.signUpForLecture(lectureId, lectureItemId, userId);

        // Then
        verify(lectureCommand, times(1)).decreaseCapacity(lectureItemId);
    }

    @Test
    @DisplayName("사용자가 신청한 모든 강의를 정상적으로 조회하는 경우")
    void findAllLecturesByUserId_success() {
        // Given
        Long userId = 1L;
        User user = new User();
        List<LectureSignUp> signUps = Arrays.asList(new LectureSignUp(1L, 1L, userId));
        List<Lecture> lectures = Arrays.asList(new Lecture(), new Lecture());

        when(userQuery.findByUserId(userId)).thenReturn(user);
        when(lectureQuery.findAllLectureSignUpByUserId(userId)).thenReturn(signUps);
        when(lectureQuery.findAllLecturesById(anyList())).thenReturn(lectures);

        // When
        List<Lecture> result = lectureUseCase.findAllLecturesByUserId(userId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(lectureQuery, times(1)).findAllLecturesById(anyList());
    }
}