package com.epam.finaltask.restcontroller;

import com.epam.finaltask.dto.UserDTO;
import com.epam.finaltask.model.LoginRequest;
import com.epam.finaltask.model.RegistrationRequest;
import com.epam.finaltask.model.Role;
import com.epam.finaltask.service.AuthService;
import com.epam.finaltask.service.JWTService;
import com.epam.finaltask.service.UserService;
import jakarta.validation.Valid;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@RestController
@CrossOrigin(origins = "http://localhost:63342")
@RequestMapping("/api/auth")
public class AuthenticationRestController {

    private final UserService userService;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationRestController(UserService userService, AuthService authService,
                                        PasswordEncoder passwordEncoder){
        this.userService = userService;
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
    }


    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerNewUser(@Valid @RequestBody RegistrationRequest registrationRequest) {
        UserDTO userDTO = UserDTO.getNewUserDTO(UUID.randomUUID().toString(),registrationRequest.getUsername(),
                passwordEncoder.encode(registrationRequest.getPassword()), registrationRequest.getRole(), null, "000 000 000", 0.0,true);
        userService.register(userDTO);
        Map<String, Object> response = new HashMap<>();
        response.put("statusCode", HttpStatus.OK);
        response.put("statusMessage", "User with name " + userDTO.getUsername() + " registered successfully!");
        response.put("generatedJWTToken", authService.authenticateUser(new LoginRequest(registrationRequest.getUsername(),
                registrationRequest.getPassword())));
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> userLogin(@Valid @RequestBody LoginRequest loginRequest) {
        String userName = loginRequest.getUsername();
        UserDTO userDTO = userService.getUserByUsername(userName);
        Map<String, Object> response = new HashMap<>();
        response.put("statusCode", HttpStatus.OK);
        response.put("statusMessage", "User with name " + userDTO.getUsername() + " logged in successfully!");
        response.put("generatedJWTToken", authService.authenticateUser(loginRequest));
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @GetMapping("/validate")
    public ResponseEntity<Map<String, Object>> userValidation() {
        Map<String, Object> response = new HashMap<>();
        response.put("statusCode", HttpStatus.OK);
        response.put("statusMessage", "User is Authenticated");
        return ResponseEntity.ok().body(response);
    }

}
