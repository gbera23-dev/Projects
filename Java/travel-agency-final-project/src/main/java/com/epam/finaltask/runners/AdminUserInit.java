package com.epam.finaltask.runners;

import com.epam.finaltask.model.Role;
import com.epam.finaltask.model.User;
import com.epam.finaltask.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;
@Component
@Order(1)
public class AdminUserInit implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminUserInit(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        User user = User.getNewUser(UUID.fromString("00000000-0000-0000-0000-000000000000"), "ADMIN", Role.ROLE_ADMIN,
                passwordEncoder.encode("ADMINADMIN"), null, "000000000", BigDecimal.valueOf(0.0),
                true);
        userRepository.save(user);
        System.out.println("Admin user successfully registered!");

    }
}
