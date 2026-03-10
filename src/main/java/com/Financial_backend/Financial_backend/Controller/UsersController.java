package com.Financial_backend.Financial_backend.Controller;

import com.Financial_backend.Financial_backend.Dto.Request.UserRegisterRequestDto;
import com.Financial_backend.Financial_backend.Dto.Response.UserRegisterResponseDto;
import com.Financial_backend.Financial_backend.Service.Users.UsersServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
//@RequiredArgsConstructor
public class UsersController {

    private final UsersServiceInterface usersServiceInterface;

    public UsersController(UsersServiceInterface usersServiceInterface){
        this.usersServiceInterface = usersServiceInterface;

    }

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponseDto> registerUser(
            @RequestBody UserRegisterRequestDto userRegisterRequestDto
            ){
        UserRegisterResponseDto userRegisterResponseDto = usersServiceInterface.register(userRegisterRequestDto);

        return ResponseEntity.ok(userRegisterResponseDto);
    }

}
