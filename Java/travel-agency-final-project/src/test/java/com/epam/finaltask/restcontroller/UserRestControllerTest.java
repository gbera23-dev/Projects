package com.epam.finaltask.restcontroller;


import com.epam.finaltask.dto.UserDTO;
import com.epam.finaltask.dto.VoucherDTO;
import com.epam.finaltask.model.Role;
import com.epam.finaltask.service.UserService;
import com.epam.finaltask.service.VoucherService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureDataJpa
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestPropertySource(properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")
public class UserRestControllerTest {
    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired

    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void getUserByIdTest() throws Exception {
        String userId = String.valueOf(UUID.randomUUID());
        UserDTO userDTO = new UserDTO(); userDTO.setId(userId);
        when(userService.getUserById(UUID.fromString(userId))).thenReturn(userDTO);

        MvcResult result = mockMvc.perform(get("/api/users/" + userId))
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();

        JsonNode rootNode = objectMapper.readTree(responseBody);
        JsonNode resultsNode = rootNode.path("results");

        UserDTO responseUserDTO = objectMapper.convertValue(resultsNode, new TypeReference<UserDTO>() {});

        assertEquals(responseUserDTO.getId(), userDTO.getId());
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void getUserByUsernameTest() throws Exception  {
        String username = "MICHAEL";
        UserDTO userDTO = new UserDTO(); userDTO.setUsername(username);
        when(userService.getUserByUsername(username)).thenReturn(userDTO);
        MvcResult result = mockMvc.perform(get("/api/users?username=" + username))
                .andExpect(status().isOk()).andReturn();
        String responseBody = result.getResponse().getContentAsString();
        JsonNode rootNode = objectMapper.readTree(responseBody);
        JsonNode resultsNode = rootNode.path("results");
        UserDTO responseUserDTO = objectMapper.convertValue(resultsNode, new TypeReference<UserDTO>() {});
        assertEquals(responseUserDTO.getUsername(), userDTO.getUsername());
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void deactivateUserTest() throws Exception {
        UserDTO userDTO = UserDTO.getNewUserDTO(UUID.randomUUID().toString(), "MICHAEL", "19283745",
                Role.ROLE_USER.toString(), null, "577543623", 200.5, true);
        UserDTO expectedUserDTO = UserDTO.getNewUserDTO(UUID.randomUUID().toString(), "MICHAEL", "19283745",
                Role.ROLE_USER.toString(), null, "577543623", 200.5, false);
        String expectedStatusCode = "OK";
        String expectedMessage = "account with username MICHAEL has been deactivated successfully!";
        when(userService.getUserById(any(UUID.class))).thenReturn(userDTO);
        when(userService.changeAccountStatus(userDTO)).thenReturn(expectedUserDTO);

        mockMvc.perform(delete("/api/users/" + userDTO.getId() )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(expectedStatusCode))
                .andExpect(jsonPath("$.statusMessage").value(expectedMessage));
    }
    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void activateUserTest() throws Exception {
        UserDTO userDTO = UserDTO.getNewUserDTO(UUID.randomUUID().toString(), "MICHAEL", "19283745",
                Role.ROLE_USER.toString(), null, "577543623", 200.5, false);
        UserDTO expectedUserDTO = UserDTO.getNewUserDTO(UUID.randomUUID().toString(), "MICHAEL", "19283745",
                Role.ROLE_USER.toString(), null, "577543623", 200.5, true);
        String expectedStatusCode = "OK";
        String expectedMessage = "account with username MICHAEL has been activated successfully!";
        when(userService.getUserById(any(UUID.class))).thenReturn(userDTO);
        when(userService.changeAccountStatus(userDTO)).thenReturn(expectedUserDTO);

        mockMvc.perform(patch("/api/users/" + userDTO.getId() )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(expectedStatusCode))
                .andExpect(jsonPath("$.statusMessage").value(expectedMessage));
    }
    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void updateUserTest() throws Exception {
        Principal principal = mock(Principal.class);
        UserDTO userDTO = UserDTO.getNewUserDTO(UUID.randomUUID().toString(), "MICHAEL", "19283745",
                Role.ROLE_USER.toString(), null, "577543623", 200.5, true);
        UserDTO updatedUserDTO = UserDTO.getNewUserDTO(userDTO.getId(), "WILLIAM", "987654321",
                Role.ROLE_USER.toString(), null, "577642313", 50.5, true);
        String expectedStatusCode = "OK";
        String expectedMessage = "account with id "+userDTO.getId()+ " has been updated successfully!";
        when(principal.getName()).thenReturn("MICHAEL");
        when(userService.getUserByUsername(any(String.class))).thenReturn(userDTO);
        when(userService.updateUser("MICHAEL", userDTO)).thenReturn(updatedUserDTO);
        mockMvc.perform(patch("/api/users/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(expectedStatusCode))
                .andExpect(jsonPath("$.statusMessage").value(expectedMessage));
    }
    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void getUserTest() throws Exception  {
        Principal principal = mock(Principal.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        String username = "MICHAEL";
        UserDTO userDTO = new UserDTO(); userDTO.setUsername(username);
        when(principal.getName()).thenReturn(username);
        when(userService.getUserByUsername(any(String.class))).thenReturn(userDTO);
        when(passwordEncoder.encode(any(String.class))).thenReturn("password");
        MvcResult result = mockMvc.perform(get("/api/users/getAuthUser"))
                .andExpect(status().isOk()).andReturn();
        String responseBody = result.getResponse().getContentAsString();
        JsonNode rootNode = objectMapper.readTree(responseBody);
        JsonNode resultsNode = rootNode.path("results");
        UserDTO responseUserDTO = objectMapper.convertValue(resultsNode, new TypeReference<UserDTO>() {});
        assertEquals(responseUserDTO.getUsername(), userDTO.getUsername());
    }

}
