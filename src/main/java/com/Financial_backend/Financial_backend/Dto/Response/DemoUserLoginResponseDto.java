package com.Financial_backend.Financial_backend.Dto.Response;


import lombok.Data;

@Data
public class DemoUserLoginResponseDto {

    private String refreshTokenDemo;
    private String accessToken;
    private String email;
    private  String message;

}
