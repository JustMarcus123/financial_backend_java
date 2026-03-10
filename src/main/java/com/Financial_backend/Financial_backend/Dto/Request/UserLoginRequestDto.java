package com.Financial_backend.Financial_backend.Dto.Request;

import lombok.Data;

@Data
public class UserLoginRequestDto {

    private String email;
    private String password;

}
