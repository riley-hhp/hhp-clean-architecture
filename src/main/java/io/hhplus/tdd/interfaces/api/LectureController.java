package io.hhplus.tdd.interfaces.api;

import io.hhplus.tdd.application.LectureUseCase;
import io.hhplus.tdd.infrastructure.lecture.entity.Lecture;
import io.hhplus.tdd.infrastructure.lecture.entity.LectureItem;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lectures")
public class LectureController {

    private final LectureUseCase lectureUseCase;

    /**
     * 신청 가능한 강좌 조회
     */
    @GetMapping("/available")
    public ResponseEntity<List<AvailableLectureResponse>> getAvailableLectures(@RequestParam Long userId) {
        List<LectureItem> availableLectureItems = lectureUseCase.findAvailableLectureItems(userId);

        List<AvailableLectureResponse> response = availableLectureItems.stream()
                .map(item -> new AvailableLectureResponse(item.getLectureId(), item.getCapacity(), item.getLectureDateTime()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * 강좌 신청
     */
    @PostMapping("/sign-up")
    public ResponseEntity<String> signUpForLecture(@RequestBody SignUpRequest signUpRequest) {
        try {
            lectureUseCase.signUpForLecture(signUpRequest.getLectureId(), signUpRequest.getLectureItemId(), signUpRequest.getUserId());
            return ResponseEntity.ok("강좌 신청이 완료되었습니다.");
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * 신청 완료한 강좌 목록 조회
     */
    @GetMapping("/signed-up")
    public ResponseEntity<List<SignedUpLectureResponse>> getSignedUpLectures(@RequestParam Long userId) {
        List<Lecture> lectures = lectureUseCase.findAllLecturesByUserId(userId);

        List<SignedUpLectureResponse> response = lectures.stream()
                .map(lecture -> new SignedUpLectureResponse(lecture.getLectureId(), lecture.getLectureName()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // 요청 클래스
    @Data
    public static class SignUpRequest {
        private Long lectureId;
        private Long lectureItemId;
        private Long userId;
    }

    // 응답 클래스 (신청 가능 강좌)
    @Data
    public static class AvailableLectureResponse {
        private Long lectureId;
        private int capacity;
        private LocalDateTime lectureDateTime;

        public AvailableLectureResponse(Long lectureId, int capacity, LocalDateTime lectureDateTime) {
            this.lectureId = lectureId;
            this.capacity = capacity;
            this.lectureDateTime = lectureDateTime;
        }
    }

    // 응답 클래스 (신청 완료 강좌)
    @Data
    public static class SignedUpLectureResponse {
        private Long lectureId;
        private String lectureName;

        public SignedUpLectureResponse(Long lectureId, String lectureName) {
            this.lectureId = lectureId;
            this.lectureName = lectureName;
        }
    }
}