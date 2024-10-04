package io.hhplus.tdd.infrastructure.user;

import io.hhplus.tdd.domain.constant.ErrorMessage;
import io.hhplus.tdd.domain.user.UserRepository;
import io.hhplus.tdd.infrastructure.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;


    public User findByUserId(Long userId) {
        return  userJpaRepository.findById(userId).orElseThrow(()-> new RuntimeException(ErrorMessage.INVALID_USER.getMessage()));
    }

    public User save(User user) {
        return userJpaRepository.save(user);
    }

}
