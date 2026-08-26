package com.simplon.tests.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.simplon.tests.entities.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, String> {

    public Optional<UserEntity> findByEmail(String email);
}
