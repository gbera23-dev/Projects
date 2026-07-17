package com.epam.finaltask.restcontroller;

import com.epam.finaltask.dto.UserDTO;
import com.epam.finaltask.service.UserService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserRestController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PreAuthorize("hasAuthority(\"ROLE_ADMIN\")")
    @GetMapping("/{userId}")
    ResponseEntity<Map<String, Object>> getUserWithId(@PathVariable String userId) {
        UserDTO userDTO = userService.getUserById(UUID.fromString(userId));
        Map<String, Object> response = new HashMap<>();
        response.put("results", userDTO);
        return ResponseEntity.ok(response);
    }
    @PreAuthorize("hasAuthority(\"ROLE_ADMIN\")")
    @GetMapping
    ResponseEntity<Map<String, Object>> getUserWithUsername(@RequestParam(value = "username") String username) {
        UserDTO userDTO = userService.getUserByUsername(username);
        Map<String, Object> response = new HashMap<>();
        response.put("results", userDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getAuthUser")
    ResponseEntity<Map<String, Object>> getUser(Principal principal) {
        UserDTO userDTO = userService.getUserByUsername(principal.getName());
        Map<String, Object> response = new HashMap<>();
        response.put("results", userDTO);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyAuthority(\"ROLE_ADMIN\", \"ROLE_MANAGER\")")
    @DeleteMapping("/{userId}")
    ResponseEntity<Map<String, Object>> deactivateUser(@PathVariable String userId) {
        UserDTO userDTO = userService.getUserById(UUID.fromString(userId));
        userDTO.setActive(false);
        userService.changeAccountStatus(userDTO);
        Map<String, Object> response = new HashMap<>();
        response.put("statusCode", HttpStatus.OK);
        response.put("statusMessage", "account with username " + userDTO.getUsername() + " has been deactivated" +
                " successfully!");
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @PreAuthorize("hasAnyAuthority(\"ROLE_ADMIN\", \"ROLE_MANAGER\")")
    @PatchMapping("/{userId}")
    ResponseEntity<Map<String, Object>> activateUser(@PathVariable String userId) {
        UserDTO userDTO = userService.getUserById(UUID.fromString(userId));
        userDTO.setActive(true);
        userService.changeAccountStatus(userDTO);
        Map<String, Object> response = new HashMap<>();
        response.put("statusCode", HttpStatus.OK);
        response.put("statusMessage", "account with username " + userDTO.getUsername() + " has been activated" +
                " successfully!");
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @PreAuthorize("hasAnyAuthority(\"ROLE_USER\", \"ROLE_ADMIN\")")
    @PatchMapping("/update")
    ResponseEntity<Map<String, Object>> updateUser(@Valid @RequestBody UserDTO userDTO, Principal principal) {
        String originalUserName = principal.getName();
        UserDTO knownUser = userService.getUserByUsername(originalUserName);
        updateInformation(knownUser, userDTO);
        UserDTO user = userService.updateUser(originalUserName, knownUser);
        Map<String, Object> response = new HashMap<>();
        response.put("statusCode", HttpStatus.OK);
        response.put("statusMessage", "account with id " + knownUser.getId() + " has been updated" +
                " successfully!");
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    void updateInformation(UserDTO knownUser, UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        String phoneNumber = userDTO.getPhoneNumber();
        Double deposit = userDTO.getBalance();
        if(username != null && !username.isEmpty()){
            knownUser.setUsername(username);
        }
        if(password != null && !password.isEmpty()) {
            knownUser.setPassword(passwordEncoder.encode(password));
        }
        if(phoneNumber != null && !phoneNumber.isEmpty())knownUser.setPhoneNumber(phoneNumber);
        if(deposit != null){
            double currentBalance = knownUser.getBalance();
            knownUser.setBalance(currentBalance + deposit);
        }
    }

}
