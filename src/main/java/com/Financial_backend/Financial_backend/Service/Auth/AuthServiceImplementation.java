package com.Financial_backend.Financial_backend.Service.Auth;

import com.Financial_backend.Financial_backend.Dto.Request.UserLoginRequestDto;
import com.Financial_backend.Financial_backend.Dto.Response.UserLoginResponseDto;
import com.Financial_backend.Financial_backend.Entity.UsersEntity;
import com.Financial_backend.Financial_backend.Respository.UsersRepository;
import com.Financial_backend.Financial_backend.Service.Users.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthServiceImplementation {

    private  final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImplementation(UsersRepository usersRepository,
                                     PasswordEncoder passwordEncoder,
                                     JwtService jwtService){
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public UserLoginResponseDto login(UserLoginRequestDto userLoginRequestDto){

        System.out.println("Login called received for:" + userLoginRequestDto.getEmail());


        //1. find the user by email
        UsersEntity user = usersRepository.findByEmail(userLoginRequestDto.getEmail())
                .orElseThrow(()-> new RuntimeException("invalid email or password"));
        System.out.println("user found in the db:" + user.getEmail());


        //2. check if the account is active
        if(Boolean.FALSE.equals(user.getIs_active())){
            throw  new RuntimeException("Account is deactivated");

        }

        //3. varified password against stored hash
        if(!passwordEncoder.matches(userLoginRequestDto.getPassword(), user.getPassword_hash())){
            System.out.println("password mismatch for:" +user.getEmail());
            throw new RuntimeException("invalid email or password");
        }

        System.out.println("password match");


        //4. update last login timestamp
        user.setLast_login_at(LocalDateTime.now().toString());
        usersRepository.save(user);

        //5.Generate jwt
        String token = jwtService.generateToken(user);
        System.out.println("Jwt generated:" +token.substring(0,20) +"...");


        System.out.println("returning response");
        //6. Build response
        UserLoginResponseDto userLoginResponseDto = new UserLoginResponseDto();
        userLoginResponseDto.setToken(token);
        userLoginResponseDto.setEmail(user.getEmail());
        userLoginResponseDto.setMessage("login successful");

        return  userLoginResponseDto;
    }


}
