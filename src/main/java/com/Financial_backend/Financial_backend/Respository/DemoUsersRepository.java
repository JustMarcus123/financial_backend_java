package com.Financial_backend.Financial_backend.Respository;

import com.Financial_backend.Financial_backend.Entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DemoUsersRepository extends JpaRepository<UsersEntity, Long> {

    Optional<UsersEntity> findByEmail(String email);

}
