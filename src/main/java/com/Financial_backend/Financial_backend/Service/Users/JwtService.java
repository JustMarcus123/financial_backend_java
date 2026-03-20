package com.Financial_backend.Financial_backend.Service.Users;

import com.Financial_backend.Financial_backend.Entity.UsersEntity;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

 @Value("${jwt.secret}")
    private String secret;

 @Value("${jwt.expiration}")
    private Long expiration;

 private Key getSigningKey(){
     return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
 }

 public String generateToken(UsersEntity usersEntity){

     System.out.println("JWT: Generating token for..."+ usersEntity.getEmail());

     Map<String, Object> claims = new HashMap<>();

     //these fields are decoded by autheContext on the frontend
     claims.put("name",usersEntity.getFirstName()+ " "+ usersEntity.getLastName());
     claims.put("role",Boolean.TRUE.equals(usersEntity.getRole())? "ADMIN":"USER");
     claims.put("firstName", usersEntity.getFirstName());

     System.out.println("JWT: token generated successfully");

     return Jwts.builder()
             .setClaims(claims)
             .setSubject(usersEntity.getEmail())
             .setIssuedAt(new Date())
             .setExpiration(new Date(System.currentTimeMillis()+ expiration))
             .signWith(getSigningKey(), SignatureAlgorithm.HS256)
             .compact();

 }

 public  String extractEmail(String token){
     return Jwts.parserBuilder()
             .setSigningKey(getSigningKey())
             .build()
             .parseClaimsJws(token)
             .getBody()
             .getSubject();
 }

 public boolean isTokenValid(String token){
     System.out.println("JWT: Validating token");
     try {
         Jwts.parserBuilder()
                 .setSigningKey(getSigningKey())
                 .build()
                 .parseClaimsJws(token);

         return true;
     }catch (Exception e){
         return false;

     }
 }


}
