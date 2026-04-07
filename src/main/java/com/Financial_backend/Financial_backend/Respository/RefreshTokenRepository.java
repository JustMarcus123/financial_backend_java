package com.Financial_backend.Financial_backend.Respository;

import com.Financial_backend.Financial_backend.Entity.RefreshTokenEntity;
import com.Financial_backend.Financial_backend.Entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    // find by token string - used on every refresh request
  Optional < RefreshTokenEntity> findByToken(String token);

    // This one will delete all sessions for a user - used on logout and password change
    //this is the revocation mechanism ---- which makes this a stateful

    @Modifying
    @Transactional
    void deleteByUsers(UsersEntity users);


    //revoke single token - used for logout this device
    @Modifying
    @Transactional
    @Query("UPDATE RefreshTokenEntity r SET r.revoked= true where r.token =:token")
    void revokedByToken( @Param("token") String token );

    //Revoked all tokens for a user - use when the password change or account locked
    @Modifying
    @Transactional
    @Query("UPDATE RefreshTokenEntity r SET r.revoked = true WHERE r.users= :users")
    void revokedAllByUser( @Param("users") UsersEntity users);

    // Cleanup job  --- delete expired token from the table
    @Modifying
    @Transactional
    void deleteByExpiresAt(LocalDateTime now);


}
