package com.Financial_backend.Financial_backend.Service.Users;

import com.Financial_backend.Financial_backend.Dto.Request.UserRegisterRequestDto;
import com.Financial_backend.Financial_backend.Dto.Response.UserRegisterResponseDto;

public interface UsersServiceInterface {

     UserRegisterResponseDto register(UserRegisterRequestDto userRegisterRequestDto);

}
