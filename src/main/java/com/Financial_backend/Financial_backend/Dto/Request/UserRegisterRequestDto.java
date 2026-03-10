package com.Financial_backend.Financial_backend.Dto.Request;

import lombok.Data;

@Data
public class UserRegisterRequestDto {

    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
//    private String message;

}
