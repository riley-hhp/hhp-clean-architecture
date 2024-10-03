package io.hhplus.tdd.domain.user;

import io.hhplus.tdd.infrastructure.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserCommand {

    private final UserRepository userRepository;


    public User save(User user) {

        return userRepository.save(user);
    }


}
