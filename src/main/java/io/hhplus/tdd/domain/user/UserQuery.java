package io.hhplus.tdd.domain.user;

import io.hhplus.tdd.domain.constant.ErrorMessage;
import io.hhplus.tdd.infrastructure.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserQuery {

    private final UserRepository userRepository;

    public User findByUserId(Long userId) {

        return Optional.ofNullable(userRepository.findByUserId(userId))
                        .orElseThrow(() -> new RuntimeException(ErrorMessage.INVALID_USER.getMessage()));
    }


}
