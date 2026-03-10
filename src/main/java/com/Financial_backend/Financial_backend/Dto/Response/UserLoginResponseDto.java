package com.Financial_backend.Financial_backend.Dto.Response;

import lombok.Data;

@Data
public class UserLoginResponseDto {

    private String token;
    private String email;
    private String message;

}
