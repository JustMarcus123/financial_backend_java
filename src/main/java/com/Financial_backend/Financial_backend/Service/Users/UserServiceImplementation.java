package com.Financial_backend.Financial_backend.Service.Users;


import com.Financial_backend.Financial_backend.Config.PasswordConfig;
import com.Financial_backend.Financial_backend.Dto.Request.UserRegisterRequestDto;
import com.Financial_backend.Financial_backend.Dto.Response.UserRegisterResponseDto;
import com.Financial_backend.Financial_backend.Entity.UsersEntity;
import com.Financial_backend.Financial_backend.Respository.UsersRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImplementation implements UsersServiceInterface {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImplementation(UsersRepository usersRepository,
                                     PasswordEncoder passwordEncoder){
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserRegisterResponseDto register(UserRegisterRequestDto userRegisterRequestDto){
        // check if email already exists
        if(usersRepository.existsByEmail(userRegisterRequestDto.getEmail())){
            throw new RuntimeException("email already exist");
        }

        UsersEntity usersEntity = new UsersEntity();

        usersEntity.setEmail(userRegisterRequestDto.getEmail());
        usersEntity.setPassword_hash(passwordEncoder.encode(userRegisterRequestDto.getPassword()));
        usersEntity.setFirstName(userRegisterRequestDto.getFirstName());
        usersEntity.setLastName(userRegisterRequestDto.getLastName());
        usersEntity.setPhone(userRegisterRequestDto.getPhone());
        usersEntity.setIs_active(true);
        usersEntity.setEmail_varified(false);

        usersRepository.save(usersEntity);

        UserRegisterResponseDto userRegisterResponseDto = new UserRegisterResponseDto();

        userRegisterResponseDto.setEmail(userRegisterRequestDto.getEmail());
        userRegisterResponseDto.setMessage("User register successful");

        return userRegisterResponseDto;
    }

}


