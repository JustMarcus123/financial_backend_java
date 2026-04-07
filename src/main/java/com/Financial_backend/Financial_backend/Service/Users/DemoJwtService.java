package com.Financial_backend.Financial_backend.Service.Users;

import com.Financial_backend.Financial_backend.Entity.UsersEntity;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Service
public class DemoJwtService {

    @Value("${jwt.secret}")
    private String secret;

    ///  short expiry access token

    @Value("${jwt.access-token-expiration}")
    private  Long DemoAccessTokenExpiration;

 private Key getSigningKey (){
     return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
 }


    public String generateDemoAccessToken(UsersEntity usersEntity){
        System.out.println("JWT: generating token for "+ usersEntity.getEmail());

        Map<String, Object> claims = new HashMap<>();

        //these fields are decoded by the authContext on the frontend
        claims.put("name",usersEntity.getFirstName()+ " " + usersEntity.getLastName());
        claims.put("role", Boolean.TRUE.equals(usersEntity.getRole())?"ADMIN":"USER");
        claims.put("firstName",usersEntity.getFirstName());
        claims.put("type","access");

        System.out.println("JWT: token generated successfully");

        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(usersEntity.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+DemoAccessTokenExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

        log.info("JWT: access token generated for :{}",usersEntity.getEmail());
        return token;
    }

    public  String extractEmail(String token){
     return extractAllClaims(token).getSubject();
    }

    public boolean isTokenValid(String token){
     try {
         Claims claims = extractAllClaims(token);
         boolean valid = claims.getExpiration().after(new Date());
         if(valid) log.debug("jwt: token Valid for:{}",claims.getSubject());
         else  log.warn("JWT: token expired for:{}",claims.getSubject());
         return valid;
     }catch (ExpiredJwtException ex){
         log.warn("JWT: expired:{}",ex.getMessage());
         return  false;

     }catch (MalformedJwtException ex){
         log.error("JWT: Malformed:{}",ex.getMessage());
         return false;
     }catch (Exception ex){
         log.error("JWT: error:{}",ex.getMessage());
         return false;
     }
    }

    private Claims extractAllClaims(String token){
     return  Jwts.parserBuilder()
             .setSigningKey(getSigningKey())
             .build()
             .parseClaimsJws(token)
             .getBody();
    }





}
