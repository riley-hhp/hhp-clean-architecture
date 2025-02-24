package io.hhplus.tdd.infrastructure.user;

import io.hhplus.tdd.infrastructure.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long> {}
