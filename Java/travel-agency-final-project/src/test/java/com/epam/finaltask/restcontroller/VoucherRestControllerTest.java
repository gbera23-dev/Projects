package com.epam.finaltask.restcontroller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

import com.epam.finaltask.dto.UserDTO;
import com.epam.finaltask.model.*;
import com.epam.finaltask.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.Filter;
import lombok.With;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import org.springframework.web.bind.annotation.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureDataJpa
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestPropertySource(properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")
public class VoucherRestControllerTest {

    @MockBean
    private VoucherService voucherService;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Qualifier("JWTFilter")
    @Autowired
    private Filter filter;

    @Test
    @WithMockUser
    void findAll_Successfully() throws Exception {
        List<VoucherDTO> voucherDTOList = new ArrayList<>();
        when(voucherService.findAll()).thenReturn(voucherDTOList);
        MvcResult result = mockMvc.perform(get("/api/vouchers"))
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();

        JsonNode rootNode = objectMapper.readTree(responseBody);
        JsonNode resultsNode = rootNode.path("results");

        List<VoucherDTO> responseVoucherDTOList = objectMapper.convertValue(resultsNode, new TypeReference<List<VoucherDTO>>() {});
        assertEquals(voucherDTOList, responseVoucherDTOList);
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void getVoucherTest() throws Exception{
        VoucherDTO voucherDTO = new VoucherDTO();
        voucherDTO.setId(UUID.randomUUID().toString());
        voucherDTO.setTitle("UpdatedTitle");
        voucherDTO.setDescription("Updated description");
        voucherDTO.setPrice(499.99);
        voucherDTO.setTourType(TourType.SAFARI.name());
        voucherDTO.setTransferType(TransferType.JEEPS.name());
        voucherDTO.setHotelType(HotelType.THREE_STARS.name());
        voucherDTO.setStatus(VoucherStatus.PAID.name());
        voucherDTO.setArrivalDate(LocalDate.of(2024, 7, 15));
        voucherDTO.setEvictionDate(LocalDate.of(2024, 7, 20));
        voucherDTO.setUserId(UUID.randomUUID());
        voucherDTO.setIsHot(true);
        String id = voucherDTO.getId();
        when(voucherService.find(voucherDTO.getId())).thenReturn(voucherDTO);

        MvcResult result = mockMvc.perform(get("/api/vouchers/" + id))
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        System.out.println(responseBody);

        JsonNode root = objectMapper.readTree(responseBody);
        VoucherDTO resultingVoucherDTO = objectMapper.treeToValue(root.get("results"), VoucherDTO.class);
        assertEquals(voucherDTO.toList(), resultingVoucherDTO.toList());
    }

    @Test
    @WithMockUser
    void findAllByUserId_Successfully() throws Exception {
        String userId = String.valueOf(UUID.randomUUID());
        List<VoucherDTO> voucherDTOList = new ArrayList<>();

        when(voucherService.findAllByUserId(userId)).thenReturn(voucherDTOList);

        MvcResult result = mockMvc.perform(get("/api/vouchers/user/" + userId))
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();

        JsonNode rootNode = objectMapper.readTree(responseBody);
        JsonNode resultsNode = rootNode.path("results");

        List<VoucherDTO> responseVoucherDTOList = objectMapper.convertValue(resultsNode, new TypeReference<List<VoucherDTO>>() {});

        assertEquals(voucherDTOList, responseVoucherDTOList);
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void createVoucher_ValidData_SuccessfullyCreated() throws Exception {
        VoucherDTO voucherDTO = new VoucherDTO();
        voucherDTO.setTitle("SummerSale2024");
        voucherDTO.setDescription("Summer Sale Voucher Description");
        voucherDTO.setPrice(299.99);
        voucherDTO.setTourType(TourType.ADVENTURE.name());
        voucherDTO.setTransferType(TransferType.PLANE.name());
        voucherDTO.setHotelType(HotelType.FIVE_STARS.name());
        voucherDTO.setStatus(VoucherStatus.PAID.name());
        voucherDTO.setArrivalDate(LocalDate.of(2024, 6, 15));
        voucherDTO.setEvictionDate(LocalDate.of(2024, 6, 20));
        voucherDTO.setUserId(UUID.randomUUID());
        voucherDTO.setIsHot(false);

        String expectedStatusCode = "OK";
        String expectedMessage = "Voucher is successfully created";

        when(voucherService.create(any(VoucherDTO.class))).thenReturn(voucherDTO);


        mockMvc.perform(post("/api/vouchers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(voucherDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(expectedStatusCode))
                .andExpect(jsonPath("$.statusMessage").value(expectedMessage));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void updateVoucher_ValidData_SuccessfullyUpdated() throws Exception {
        VoucherDTO voucherDTO = new VoucherDTO();
        voucherDTO.setTitle("UpdatedTitle");
        voucherDTO.setDescription("Updated description");
        voucherDTO.setPrice(499.99);
        voucherDTO.setTourType(TourType.SAFARI.name());
        voucherDTO.setTransferType(TransferType.JEEPS.name());
        voucherDTO.setHotelType(HotelType.THREE_STARS.name());
        voucherDTO.setStatus(VoucherStatus.PAID.name());
        voucherDTO.setArrivalDate(LocalDate.of(2024, 7, 15));
        voucherDTO.setEvictionDate(LocalDate.of(2024, 7, 20));
        voucherDTO.setUserId(UUID.randomUUID());
        voucherDTO.setIsHot(true);

        String voucherId = String.valueOf(UUID.randomUUID());
        String expectedStatusCode = "OK";
        String expectedMessage = "Voucher is successfully updated";

        when(voucherService.update(eq(voucherId), any(VoucherDTO.class))).thenReturn(voucherDTO);

        mockMvc.perform(patch("/api/vouchers/" + voucherId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(voucherDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(expectedStatusCode))
                .andExpect(jsonPath("$.statusMessage").value(expectedMessage));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void deleteVoucherById_VoucherExists_SuccessfullyDeleted() throws Exception {
        String voucherId = String.valueOf(UUID.randomUUID());
        String expectedStatusCode = "OK";
        String expectedMessage = String.format("Voucher with Id %s has been deleted", voucherId);

        doNothing().when(voucherService).delete(voucherId);

        mockMvc.perform(delete("/api/vouchers/" + voucherId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(expectedStatusCode))
                .andExpect(jsonPath("$.statusMessage").value(expectedMessage));

        verify(voucherService, times(1)).delete(voucherId);
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN", "ROLE_MANAGER"})
    void changeVoucherStatus_ValidData_SuccessfullyChanged() throws Exception {
        VoucherDTO voucherDTO = new VoucherDTO();
        voucherDTO.setIsHot(true);

        String voucherId = String.valueOf(UUID.randomUUID());
        String expectedStatusCode = "OK";
        String expectedMessage = "Voucher status is successfully changed";

        when(voucherService.changeHotStatus(eq(voucherId), any(VoucherDTO.class))).thenReturn(voucherDTO);

        mockMvc.perform(patch("/api/vouchers/" + voucherId + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(voucherDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(expectedStatusCode))
                .andExpect(jsonPath("$.statusMessage").value(expectedMessage));

        verify(voucherService, times(1)).changeHotStatus(eq(voucherId), any(VoucherDTO.class));
    }

    @Test
    @WithMockUser
    void testOrderVoucher() throws Exception {
        Principal principal = mock(Principal.class);
        UserDTO userDTO = new UserDTO(); userDTO.setId(UUID.randomUUID().toString());
        userDTO.setUsername("user");
        when(principal.getName()).thenReturn("user");
        VoucherDTO voucherDTO = new VoucherDTO(); voucherDTO.setId(UUID.randomUUID().toString());
        voucherDTO.setUserId(UUID.fromString(userDTO.getId()));

        when(userService.getUserByUsername(any(String.class)))
                .thenReturn(userDTO);
        when(voucherService.order(voucherDTO.getId(), userDTO.getId()))
                .thenReturn(any(VoucherDTO.class));
        String expectedStatusCode = "OK";
        String expectedMessage = "Voucher with id " + voucherDTO.getId() + " has been ordered by user with name: user";

        mockMvc.perform(post("/api/vouchers/order/"+voucherDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(expectedStatusCode))
                .andExpect(jsonPath("$.statusMessage").value(expectedMessage));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void testChangeVoucherCondition() throws Exception {
        VoucherDTO voucherDTO = new VoucherDTO();
        voucherDTO.setStatus("REGISTERED");

        String voucherId = String.valueOf(UUID.randomUUID());
        String expectedStatusCode = "OK";
        String expectedMessage = "Voucher condition is successfully changed";

        when(voucherService.changeCondition(eq(voucherId), any(VoucherDTO.class))).thenReturn(voucherDTO);

        mockMvc.perform(patch("/api/vouchers/" + voucherId + "/condition")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(voucherDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(expectedStatusCode))
                .andExpect(jsonPath("$.statusMessage").value(expectedMessage));

        verify(voucherService, times(1)).changeCondition(eq(voucherId), any(VoucherDTO.class));
    }

    @Test
    @WithMockUser
    void testFilterVouchers() throws Exception {
        VouchersFilterRequest vouchersFilterRequest = new VouchersFilterRequest();
        List<VoucherDTO> filteredList = List.of(new VoucherDTO(), new VoucherDTO());
        VoucherDTO voucherDTO1 = filteredList.get(0);
        VoucherDTO voucherDTO2 = filteredList.get(1);
        voucherDTO1.setTitle("title1"); voucherDTO1.setDescription("description1");
        voucherDTO2.setTitle("title2"); voucherDTO2.setDescription("description2");
        when(voucherService.filter(any(VouchersFilterRequest.class))).thenReturn(filteredList);

        MvcResult result = mockMvc.perform(post("/api/vouchers/filter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(vouchersFilterRequest)))
                        .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();

        JsonNode rootNode = objectMapper.readTree(responseBody);
        JsonNode resultsNode = rootNode.path("results");

        List<VoucherDTO> responseVoucherDTOList = objectMapper.convertValue(resultsNode, new TypeReference<List<VoucherDTO>>() {});
        assertEquals(filteredList.get(0).toString(), responseVoucherDTOList.get(0).toString(), "vouchers must be equal");
        assertEquals(filteredList.get(1).toString(), responseVoucherDTOList.get(1).toString(), "vouchers must be equal");
    }

}
