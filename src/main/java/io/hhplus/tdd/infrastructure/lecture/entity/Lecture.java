package io.hhplus.tdd.infrastructure.lecture.entity;

import io.hhplus.tdd.infrastructure.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@RequiredArgsConstructor
public class Lecture extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lectureId;
    private String lectureName;
    private String teacherName;

    public Lecture(String lectureName, String teacherName) {
        this.lectureName = lectureName;
        this.teacherName = teacherName;
    }
}
