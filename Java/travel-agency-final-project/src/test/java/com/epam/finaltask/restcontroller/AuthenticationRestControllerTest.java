package com.epam.finaltask.restcontroller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.*;

import com.epam.finaltask.dto.UserDTO;
import com.epam.finaltask.model.*;
import com.epam.finaltask.service.AuthService;
import com.epam.finaltask.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.epam.finaltask.dto.VoucherDTO;
import com.epam.finaltask.service.VoucherService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureDataJpa
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestPropertySource(properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")
public class AuthenticationRestControllerTest {
    @MockBean
    private UserService userService;

    @MockBean
    private AuthService authService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void registerNewUserTest() throws Exception{
        UserDTO userDTO = UserDTO.getNewUserDTO(UUID.randomUUID().toString(), "MICHAEL", "19283745",
                Role.ROLE_USER.toString(), null, null, null, true);
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("MICHAEL"); registrationRequest.setPassword("19283745");
        registrationRequest.setRepeatedPassword(registrationRequest.getPassword());
        registrationRequest.setRole("ROLE_USER");
        String expectedStatusCode = "OK";
        String expectedStatusMessage = "User with name MICHAEL registered successfully!";
        String expectedGeneratedJTWToken = "someRandomJWTToken";
        when(userService.register(any(UserDTO.class))).thenReturn(userDTO);
        when(authService.authenticateUser(any(LoginRequest.class))).thenReturn(expectedGeneratedJTWToken);
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(registrationRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(expectedStatusCode))
                .andExpect(jsonPath("$.statusMessage").value(expectedStatusMessage))
                .andExpect(jsonPath("$.generatedJWTToken").isString());
    }

    @Test
    void userLoginTest() throws Exception {
        UserDTO userDTO = UserDTO.getNewUserDTO(UUID.randomUUID().toString(), "MICHAEL", "19283745",
                Role.ROLE_USER.toString(), null, null, null, true);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(userDTO.getUsername()); loginRequest.setPassword(userDTO.getPassword());
        String expectedStatusCode  = HttpStatus.valueOf(200).name();
        String expectedStatusMessage = "User with name MICHAEL logged in successfully!";
        String expectedGeneratedJTWToken = "someRandomJWTToken";
        when(userService.getUserByUsername("MICHAEL")).thenReturn(userDTO);
        when(authService.authenticateUser(any(LoginRequest.class))).thenReturn(expectedGeneratedJTWToken);
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(loginRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(expectedStatusCode))
                .andExpect(jsonPath("$.statusMessage").value(expectedStatusMessage))
                .andExpect(jsonPath("$.generatedJWTToken").isString());
    }

    @Test
    void userValidateTest() throws Exception {
        String expectedStatusCode  = "OK";
        String expectedStatusMessage = "User is Authenticated";
        mockMvc.perform(get("/api/auth/validate")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(expectedStatusCode))
                .andExpect(jsonPath("$.statusMessage").value(expectedStatusMessage));
    }

}
