package com.Financial_backend.Financial_backend.Dto.Response;

import lombok.Data;

@Data
public class UserRegisterResponseDto {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String message;

}
