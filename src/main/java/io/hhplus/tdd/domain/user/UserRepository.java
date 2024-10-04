package io.hhplus.tdd.domain.user;

import io.hhplus.tdd.infrastructure.user.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {

    public User findByUserId(Long userId);

    public User save(User user);
}
