package com.Financial_backend.Financial_backend.Respository;

import com.Financial_backend.Financial_backend.Entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<UsersEntity, Long> {

    // creating a custom methode since we might find the user by email or phone

    //to check whether the email is unigue or not
    Optional<UsersEntity> findByEmail(String email);


    //to varify the email
    Boolean existsByEmail(String email);

    Optional<UsersEntity> findByPhone(String phone);

}
