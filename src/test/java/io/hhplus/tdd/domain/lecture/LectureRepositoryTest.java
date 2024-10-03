package io.hhplus.tdd.domain.lecture;

import io.hhplus.tdd.infrastructure.lecture.entity.LectureItem;
import io.hhplus.tdd.infrastructure.lecture.entity.LectureSignUp;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("LectureRepositoryTest 단위테스트")
class LectureRepositoryTest {

    @Mock
    LectureRepository lectureRepository;

    @Test
    @DisplayName("특정 사용자와 강의 항목 ID로 강의 신청 정보를 검색하는 메서드를 테스트")
    void findLectureSignUpByUserIdAndLectureItemId() {
        // Given
        Long userId = 1L;
        Long lectureItemId = 1L;
        LectureSignUp expectedSignUp = new LectureSignUp();
        when(lectureRepository.findLectureSignUpByUserIdAndLectureItemId(userId, lectureItemId)).thenReturn(expectedSignUp);

        // When
        LectureSignUp result = lectureRepository.findLectureSignUpByUserIdAndLectureItemId(userId, lectureItemId);

        // Then
        assertNotNull(result);
        verify(lectureRepository, times(1)).findLectureSignUpByUserIdAndLectureItemId(userId, lectureItemId);
    }

    @Test
    @DisplayName("현재 날짜를 기준으로 신청 가능한 강의 항목을 검색하는 메서드를 테스트")
    void findAvailableLectureItems() {
        // Given
        LocalDateTime today = LocalDateTime.now();
        List<LectureItem> expectedItems = Arrays.asList(new LectureItem(), new LectureItem());
        when(lectureRepository.findAvailableLectureItems(today)).thenReturn(expectedItems);

        // When
        List<LectureItem> result = lectureRepository.findAvailableLectureItems(today);

        // Then
        assertEquals(2, result.size());
        verify(lectureRepository, times(1)).findAvailableLectureItems(today);
    }

    @Test
    @DisplayName("특정 사용자에 대한 모든 강의 신청 정보를 검색하는 메서드를 테스트")
    void findAllLectureSignUpByUserId() {
        // Given
        Long userId = 1L;
        List<LectureSignUp> expectedSignUps = Arrays.asList(new LectureSignUp(), new LectureSignUp());
        when(lectureRepository.findAllLectureSignUpByUserId(userId)).thenReturn(expectedSignUps);

        // When
        List<LectureSignUp> result = lectureRepository.findAllLectureSignUpByUserId(userId);

        // Then
        assertEquals(2, result.size());
        verify(lectureRepository, times(1)).findAllLectureSignUpByUserId(userId);
    }

    @Test
    @DisplayName("강의를 신청하기 위해 강의 아이템을 찾는 메서드를 테스트")
    void findLectureItemForSignUp() {
        // Given
        Long lectureItemId = 1L;
        LectureItem expectedItem = new LectureItem();
        when(lectureRepository.findLectureItemForSignUp(lectureItemId)).thenReturn(expectedItem);

        // When
        LectureItem result = lectureRepository.findLectureItemForSignUp(lectureItemId);

        // Then
        assertNotNull(result);
        verify(lectureRepository, times(1)).findLectureItemForSignUp(lectureItemId);
    }

    @Test
    @DisplayName("강의를 신청하는 메서드를 테스트")
    void signupForLecture() {
        // Given
        Long lectureId = 1L;
        Long lectureItemId = 1L;
        Long userId = 1L;
        Long expectedSignUpId = 123L;
        when(lectureRepository.signupForLecture(lectureId, lectureItemId, userId)).thenReturn(expectedSignUpId);

        // When
        Long result = lectureRepository.signupForLecture(lectureId, lectureItemId, userId);

        // Then
        assertEquals(expectedSignUpId, result);
        verify(lectureRepository, times(1)).signupForLecture(lectureId, lectureItemId, userId);
    }

    //    TODO) STEP3
//    @Test
//    @DisplayName("강의 항목의 정원을 줄이는 메서드를 테스트")
//    void decreaseCapacityByLectureItem() {
//        // Given
//        Long lectureItemId = 1L;
//        doNothing().when(lectureRepository).decreaseCapacityByLectureItem(lectureItemId);
//
//        // When
//        lectureRepository.decreaseCapacityByLectureItem(lectureItemId);
//
//        // Then
//        verify(lectureRepository, times(1)).decreaseCapacityByLectureItem(lectureItemId);
//    }
}