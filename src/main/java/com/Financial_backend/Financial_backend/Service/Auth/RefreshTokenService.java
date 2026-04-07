/**
 * This is STATEFUL SERvICE layer
 *
 * Every method here either reads or writes to the refresh_token table
 * That DB interaction is what makes this a STATElLESS
 *
 *  STATELESS would be: validate token by signature alone, no DB
 *   STATEFUL  is:  validate token by checking it EXISTS in DB + not revoked
 *
 * The key insight: even if a refresh token has a valid signature
 * if it's been deleted or revoked in this table then access is denied
 *
 * the server has memory of what sessions are valid
 *
 *
 * */


package com.Financial_backend.Financial_backend.Service.Auth;

import com.Financial_backend.Financial_backend.Entity.RefreshTokenEntity;
import com.Financial_backend.Financial_backend.Entity.UsersEntity;
import com.Financial_backend.Financial_backend.Respository.RefreshTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service

public class RefreshTokenService {


    private  final RefreshTokenRepository refreshTokenRepository;

    //7 days in milliseconds
    @Value("${jwt.refresh-token-expiration}")
    private Long refreshTokenExpiration;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository){
        this.refreshTokenRepository = refreshTokenRepository;
    }

    // create
    // called on every login -- creates a new session row in DB

    public RefreshTokenEntity createRefreshToken(UsersEntity users, String deviceInfo){
        log.info("[RefreshToken] session created for :{}" ,users.getEmail());

        RefreshTokenEntity refreshToken = RefreshTokenEntity.builder()
                .token(UUID.randomUUID().toString())    // random uuid not a string
                .users(users)
                .expiresAt(LocalDateTime.now().plusSeconds(refreshTokenExpiration / 1000))
                .revoked(false)
                .deviceInfo(deviceInfo)
                .build();

        refreshTokenRepository.save(refreshToken);


        //STATEFUL INDICATOR: we just wrote to the DB
        // now the servers know this session exists

        log.info("[RefreshToken] session created for :{} | expiresAt:{}");

        return refreshToken;

    }

    //Validate
    // STATEFUL INDICATOR: validates by db lookup, not my signature

    public RefreshTokenEntity validateRefreshToken(String token){
        log.info("RefreshToken Validating token.....");

        //step 1: does it exist in db? (STATEFUL CHECK)
        RefreshTokenEntity refreshToken = refreshTokenRepository.findByToken(token)
        .orElseThrow(()->{
            log.warn("RefreshToken no token found in the db ------- already used or never existed");

            return  new RuntimeException("Invalid refresh token");
        });

        // step 2: has it been revoked? (STATEFUL CHECK)
        if(Boolean.TRUE.equals(refreshToken.getRevoked())){
            log.warn("Refresh token was revoked for user:{}"
            );
            throw  new RuntimeException("Session has been revoked please login again");
        }

        //step 3:  check if it's expired?
        if(refreshToken.getExpiresAt().isBefore(LocalDateTime.now())){
            log.warn("RefreshToken is expired");

            refreshTokenRepository.delete(refreshToken);
        }


        log.info("✅ [RefreshToken] Token valid for");


        return  refreshToken;
    }

    // revoke single session
    //called on logout   --- immediately invalidates this session

    public void revokedToken(String token){
        log.info("RefreshToken Revoking single session.....");
        refreshTokenRepository.revokedByToken(token);
        log.info("RefreshToken Token revoke");
    }

    //Revoke all sessions for a user
    //called on: password change, account lock, suspicious activity
    public  void  revokeAllUserTokens(UsersEntity users){
        log.info("RefreshToken Revoking all session for :{}");
        refreshTokenRepository.revokedAllByUser(users);
        log.info("RefreshToken All session Revoked");
    }

    // scheduled cleanup
    //Runs Every day ----- delete all expires token to keep the table clean
    @Scheduled(cron = "0 0 0 * * *")
    public void cleanExpiredToken(){
        log.info("Running scheduled cleanup of expired tokens...");
        refreshTokenRepository.deleteByExpiresAt(LocalDateTime.now());
    }

}
