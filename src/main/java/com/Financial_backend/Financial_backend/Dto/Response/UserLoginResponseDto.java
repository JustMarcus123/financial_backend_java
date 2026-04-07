package com.Financial_backend.Financial_backend.Dto.Response;

import lombok.Data;

@Data
public class UserLoginResponseDto {

    private String accessToken;
    private String refreshToken;
    private String email;
    private String message;

}
