package com.Financial_backend.Financial_backend.Controller;

import com.Financial_backend.Financial_backend.Dto.Request.UserLoginRequestDto;
import com.Financial_backend.Financial_backend.Dto.Request.UserRegisterRequestDto;
import com.Financial_backend.Financial_backend.Dto.Response.UserLoginResponseDto;
import com.Financial_backend.Financial_backend.Service.Auth.AuthServiceImplementation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthServiceImplementation authServiceImplementation;

    public AuthController (AuthServiceImplementation authServiceImplementation){
        this.authServiceImplementation =authServiceImplementation;
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDto> login(
            @RequestBody UserLoginRequestDto userLoginRequestDto, HttpServletRequest httpRequest
            ){
        UserLoginResponseDto response = authServiceImplementation.login(userLoginRequestDto, httpRequest);
        return ResponseEntity.ok(response);
    }


    //POST /api/auth/refresh
    //Frontend calls this silently when the access token is expired
    //Body ---- refreshToken: uuid-string
    //Returns:{accessToken,refreshToken,email,message}

    @PostMapping("/refresh")
    public ResponseEntity<UserLoginResponseDto> refresh(
            @RequestBody Map<String, String> body){
        String refreshToken =body.get("refreshToken");
        UserLoginResponseDto response = authServiceImplementation.refresh(refreshToken);
        return ResponseEntity.ok(response);
    }


    //POST /api/auth/logout
    //revoke the refresh token --- no new access token after this
    //Body --- refreshToken: uuid-string

    @PostMapping("/logout")
    public ResponseEntity<Map<String,String>> logout(
            @RequestBody Map<String, String> body
    ){
        String refreshToken = body.get("refreshToken");
        authServiceImplementation.logout(refreshToken);
        return ResponseEntity.ok(Map.of("message","Loggged out succesfully"));
    }



}
