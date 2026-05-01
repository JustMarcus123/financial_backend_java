package com.Financial_backend.Financial_backend.Respository;

import com.Financial_backend.Financial_backend.Dto.Response.EmployeeResponseDto;
import com.Financial_backend.Financial_backend.Entity.SponsorEntity;
import com.Financial_backend.Financial_backend.Entity.UsersEntity;
import com.Financial_backend.Financial_backend.Enum.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<UsersEntity, Long> {

    // creating a custom methode since we might find the user by email or phone

    //to check whether the email is unique or not
    Optional<UsersEntity> findByEmail(String email);


    //to varify the email
    Boolean existsByEmail(String email);

    Optional<UsersEntity> findByPhone(String phone);

    //finding the employee from this sponsor
    @Query("SELECT u FROM UsersEntity u WHERE u.sponsor = :sponsor AND u.role = :role")
    List <UsersEntity> findEmployeeBySponsor(
            @Param("sponsor")SponsorEntity sponsor,
            @Param("role")Role role
            );

    @Query("SELECT u FROM UsersEntity u LEFT JOIN FETCH u.sponsor WHERE u.email = :email")
    Optional<UsersEntity> findByEmailWithSponsor(@Param("email") String email);


}
