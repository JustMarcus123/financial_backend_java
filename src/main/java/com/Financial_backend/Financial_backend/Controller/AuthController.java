package com.Financial_backend.Financial_backend.Controller;

import com.Financial_backend.Financial_backend.Dto.Request.UserLoginRequestDto;
import com.Financial_backend.Financial_backend.Dto.Request.UserRegisterRequestDto;
import com.Financial_backend.Financial_backend.Dto.Response.UserLoginResponseDto;
import com.Financial_backend.Financial_backend.Service.Auth.AuthServiceImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthServiceImplementation authServiceImplementation;

    public AuthController (AuthServiceImplementation authServiceImplementation){
        this.authServiceImplementation =authServiceImplementation;
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDto> login(
            @RequestBody UserLoginRequestDto userLoginRequestDto
            ){
        UserLoginResponseDto response = authServiceImplementation.login(userLoginRequestDto);
        return ResponseEntity.ok(response);
    }

}
