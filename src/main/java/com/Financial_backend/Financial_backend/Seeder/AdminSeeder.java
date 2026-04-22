package com.Financial_backend.Financial_backend.Seeder;

import com.Financial_backend.Financial_backend.Entity.UsersEntity;
import com.Financial_backend.Financial_backend.Enum.Role;
import com.Financial_backend.Financial_backend.Respository.UsersRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminSeeder implements ApplicationRunner {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;


    public AdminSeeder(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        String adminEmail = "pewdidpiepie661@gmail.com";

        if (usersRepository.existsByEmail(adminEmail)) {
            System.out.println("✅ Super admin already exists — skipping");
            return;
        }

        UsersEntity admin = UsersEntity.builder()
                .email(adminEmail)
                .password_hash(passwordEncoder.encode("Admin@123"))
                .firstName("Super")
                .lastName("Admin")
                .role(Role.SUPER_ADMIN)
                .is_active(true)
                .mfa_enable(false)
                .email_varified(true)
                .build();

        usersRepository.save(admin);
        System.out.println("✅ Super admin seeded — email: " + adminEmail);
    }
}