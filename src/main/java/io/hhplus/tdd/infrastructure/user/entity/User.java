package io.hhplus.tdd.infrastructure.user.entity;

import io.hhplus.tdd.infrastructure.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "USERS")
@NoArgsConstructor
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String userName;

    public User(Long userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

}
