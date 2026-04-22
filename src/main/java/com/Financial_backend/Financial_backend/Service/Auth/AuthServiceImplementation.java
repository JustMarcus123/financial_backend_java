package com.Financial_backend.Financial_backend.Service.Auth;

import com.Financial_backend.Financial_backend.Dto.Request.UserLoginRequestDto;
import com.Financial_backend.Financial_backend.Dto.Response.UserLoginResponseDto;
import com.Financial_backend.Financial_backend.Entity.RefreshTokenEntity;
import com.Financial_backend.Financial_backend.Entity.UsersEntity;
import com.Financial_backend.Financial_backend.Respository.UsersRepository;
import com.Financial_backend.Financial_backend.Service.Users.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class AuthServiceImplementation {

    private  final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthServiceImplementation(UsersRepository usersRepository,
                                     PasswordEncoder passwordEncoder,
                                     JwtService jwtService,
                                     RefreshTokenService refreshTokenService){
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService= refreshTokenService;
    }

    // Login
   public UserLoginResponseDto login(UserLoginRequestDto userLoginRequestDto, HttpServletRequest httpRequest){

        System.out.println("1. Login called for "+ userLoginRequestDto.getEmail());

        //find the user
       UsersEntity users = usersRepository.findByEmail(userLoginRequestDto.getEmail())
           .orElseThrow(()->{
               System.out.println("2. user found:"+ userLoginRequestDto.getEmail());
               return  new RuntimeException("invalid password or email");
           });
           System.out.println("2. users found:" +users.getEmail());


           //check if it's active
       if(Boolean.FALSE.equals(users.getIs_active())){
           throw new RuntimeException("Account is deactivated:");
       }

       //varify password
       if(!passwordEncoder.matches(userLoginRequestDto.getPassword(), users.getPassword_hash())){
           System.out.println("password mismatch");
           throw new RuntimeException("Invalid email or password");
       }

       //update last login
       users.setLast_login_at(LocalDateTime.now().toString());
       usersRepository.save(users);

       //now generate SHORT-LIVED access token (15 mins)
       String accessToken = jwtService.generateAccessToken(users);
       System.out.println("access token generated for 15 mins"+users.getEmail());

       //Create a Long lived refresh token in bd for 7days ----- stateful
       String deviceInfo = httpRequest.getHeader("User-agent");
       RefreshTokenEntity refreshTokenEntity = refreshTokenService.createRefreshToken(users,deviceInfo);

       System.out.println("Refresh token saved to db for 7 days --- stateful");

       UserLoginResponseDto response = new UserLoginResponseDto();
       response.setAccessToken(accessToken);
       response.setRefreshToken(refreshTokenEntity.getToken());
       response.setEmail(users.getEmail());
       response.setRole(users.getRole().name());
       response.setMessage("Login successful");

       return response;
    }


    //REFRESH

    //frontend calls this silently when access token expires
    public UserLoginResponseDto refresh(String refreshTokenStr){
        log.warn("auth token refresh requested");

        //stateful: validates by db lookup - if revoked/deleted ->rejected
        RefreshTokenEntity refreshTokenEntity = refreshTokenService.validateRefreshToken(refreshTokenStr);
        UsersEntity users = refreshTokenEntity.getUsers();

        //issue bew access token
        String newAccessToken = jwtService.generateAccessToken(users);
        log.warn("New access token issued for:");

        UserLoginResponseDto response = new UserLoginResponseDto();
        response.setAccessToken(newAccessToken);
        response.setRefreshToken(refreshTokenStr);
        response.setEmail(users.getEmail());
        response.setMessage("Token Refreshed");

        return response;
    }





    //logout

    public void logout(String refreshTokenStr){
        log.info("logout requested");
        refreshTokenService.revokedToken(refreshTokenStr);
        log.info("Session revoked ---- user logout");
    }
}