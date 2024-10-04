package io.hhplus.tdd.infrastructure.lecture.entity;

import io.hhplus.tdd.infrastructure.base.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class LectureItem extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lectureItemId;
    private Long lectureId;
    private LocalDateTime lectureDateTime;
    private Integer capacity;

    public LectureItem() {
        this.capacity = 30; // 기본 용량 설정
    }

    public LectureItem(Long lectureId, LocalDateTime lectureDateTime) {
        this.lectureId = lectureId;
        this.lectureDateTime = lectureDateTime;
        this.capacity = 30;
    }

    public void decreaseCapacity() {
        this.capacity -= 1;
    }
}
