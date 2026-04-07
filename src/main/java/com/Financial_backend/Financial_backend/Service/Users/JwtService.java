package com.Financial_backend.Financial_backend.Service.Users;

import com.Financial_backend.Financial_backend.Entity.UsersEntity;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SignatureException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class JwtService {

 @Value("${jwt.secret}")
    private String secret;


 //SHORT expiry 15mins or something, because refresh token handles re-auth
    //this is where it differs with stateless, if this only exist and no refresh token then it is stateless
 @Value("${jwt.access-token-expiration}")
    private Long accessTokenExpiration;

 private Key getSigningKey(){
     return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
 }

 public String generateAccessToken(UsersEntity usersEntity){

     System.out.println("JWT: Generating token for..."+ usersEntity.getEmail());

     Map<String, Object> claims = new HashMap<>();

     //these fields are decoded by authContext on the frontend
     claims.put("name",usersEntity.getFirstName()+ " "+ usersEntity.getLastName());
     claims.put("role",Boolean.TRUE.equals(usersEntity.getRole())? "ADMIN":"USER");
     claims.put("firstName", usersEntity.getFirstName());
     claims.put("type","access");

     System.out.println("JWT: token generated successfully");

     String token =Jwts.builder()
             .setClaims(claims)
             .setSubject(usersEntity.getEmail())
             .setIssuedAt(new Date())
             .setExpiration(new Date(System.currentTimeMillis()+ accessTokenExpiration))
             .signWith(getSigningKey(), SignatureAlgorithm.HS256)
             .compact();

     log.info("jwt access token generated for:{} ",usersEntity.getEmail());
     return token;
 }

 public  String extractEmail(String token){
    return extractAllClaims(token).getSubject();
 }

    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractAllClaims(token);
            boolean valid = claims.getExpiration().after(new Date());
            if (valid) log.debug(" [JWT] Token valid for: {}", claims.getSubject());
            else log.warn(" [JWT] Token expired for: {}", claims.getSubject());
            return valid;
        } catch (ExpiredJwtException ex) {
            log.warn(" [JWT] Expired: {}", ex.getMessage());
            return false;
        } catch (MalformedJwtException ex) {
            log.error("[JWT] Malformed: {}", ex.getMessage());
            return false;
        } catch (Exception ex) {
            log.error(" [JWT] Error: {}", ex.getMessage());
            return false;
        }
    }

    private Claims extractAllClaims(String token){
     return Jwts.parserBuilder()
             .setSigningKey(getSigningKey())
             .build()
             .parseClaimsJws(token)
             .getBody();
    }


}
