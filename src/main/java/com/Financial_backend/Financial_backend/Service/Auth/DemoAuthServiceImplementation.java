//package com.Financial_backend.Financial_backend.Service.Auth;
//
//import com.Financial_backend.Financial_backend.Dto.Request.DemoUserLoginRequestDto;
//import com.Financial_backend.Financial_backend.Dto.Response.DemoUserLoginResponseDto;
//import com.Financial_backend.Financial_backend.Entity.DemoRefreshEntity;
//import com.Financial_backend.Financial_backend.Entity.UsersEntity;
//import com.Financial_backend.Financial_backend.Respository.DemoRefreshRepository;
//import com.Financial_backend.Financial_backend.Respository.DemoUsersRepository;
//import com.Financial_backend.Financial_backend.Service.Users.DemoJwtService;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//
//@Service
//public class DemoAuthServiceImplementation {
//
//
//    private final DemoRefreshRepository demoRefreshRepository;
//    private  final DemoUsersRepository demoUsersRepository;
//    private final PasswordEncoder passwordEncoder;
//    private  final DemoJwtService demoJwtService;
//
//
//
//    public DemoAuthServiceImplementation (
//            DemoRefreshRepository demoRefreshRepository , DemoUsersRepository demoUsersRepository, PasswordEncoder passwordEncoder, DemoJwtService demoJwtService){
//
//        this.demoRefreshRepository = demoRefreshRepository;
//        this.demoUsersRepository = demoUsersRepository;
//        this.passwordEncoder = passwordEncoder;
//        this.demoJwtService= demoJwtService;
//    }
//
//
//    //login
//
//    //firstly declare the method
//    public DemoUserLoginResponseDto login(DemoUserLoginRequestDto demoUserLoginRequestDto){
//
//        System.out.println("Logging request from"+ demoUserLoginRequestDto.getEmail());
//
//        //now find the user using the email?!!!!!
//        UsersEntity loggingUser = demoUsersRepository.findByEmail(demoUserLoginRequestDto.getEmail())
//                .orElseThrow(()->{
//                    System.out.println("logging request for "+ demoUserLoginRequestDto.getEmail());
//                    return new RuntimeException("invalid username or password");
//                });
//
//        System.out.println("User found"+loggingUser.getEmail());
//
//        // check of the password is valid for the requested user......
//        if(!passwordEncoder.matches(demoUserLoginRequestDto.getPassword(), loggingUser.getPassword_hash())){
//            System.out.println("password mismatch");
//            throw new RuntimeException("Invalid email or password");
//        }
//
//        //update the last login
//         loggingUser.setLast_login_at(LocalDateTime.now().toString());
//        demoUsersRepository.save(loggingUser);
//
//        //now time to generate the access token
//      String  accessToken = demoJwtService.generateDemoAccessToken(loggingUser);
//      System.out.println("JWT: accessToken successfully generated for:"+loggingUser.getEmail());
//
//      //create a long-lived refresh token
//
//
//
//    }
//
//}
