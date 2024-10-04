package io.hhplus.tdd.infrastructure.lecture.entity;

import io.hhplus.tdd.infrastructure.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "LECTURE_SIGNUP", uniqueConstraints = {@UniqueConstraint(columnNames = {"lectureItemId", "userId"})})
@RequiredArgsConstructor
public class LectureSignUp extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long signupId;
    private Long lectureId;
    private Long lectureItemId;
    private Long userId;

    public LectureSignUp(Long lectureId, Long lectureItemId, Long userId) {
        this.lectureId = lectureId;
        this.lectureItemId = lectureItemId;
        this.userId = userId;
    }

}
