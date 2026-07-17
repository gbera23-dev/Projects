package com.epam.finaltask.service;


import com.epam.finaltask.Utils.RandomVoucherGenerator;
import com.epam.finaltask.dto.UserDTO;
import com.epam.finaltask.dto.VoucherDTO;
import com.epam.finaltask.mapper.UserMapper;
import com.epam.finaltask.mapper.VoucherMapper;
import com.epam.finaltask.model.*;
import com.epam.finaltask.repository.VoucherRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VoucherServiceImplTest {
    @Mock
    private VoucherRepository voucherRepository;

    @Mock
    private UserService userService;

    @Mock
    private VoucherMapper voucherMapper;

    @Mock
    private  UserMapper userMapper;

    @InjectMocks
    private VoucherServiceImpl voucherService;

    VoucherDTO voucherDTO;
    Voucher voucher;
    @BeforeEach
    void getVoucherAndDTO() {
         voucherDTO = VoucherDTO.getNewVoucherDTO(UUID.randomUUID().toString(), "Title","Description",
                499.99, TourType.SAFARI.name(),TransferType.JEEPS.name(),HotelType.THREE_STARS.name(),
                VoucherStatus.PAID.name(), LocalDate.of(2024, 7, 15), LocalDate.of(2024, 7, 20),
                UUID.randomUUID(), true);
         voucher = Voucher.getNewVoucher("Title", UUID.fromString(voucherDTO.getId()), voucherDTO.getPrice(),
                voucherDTO.getDescription(), TransferType.valueOf(voucherDTO.getTransferType()),
                TourType.valueOf(voucherDTO.getTourType()), HotelType.valueOf(voucherDTO.getHotelType()),
                VoucherStatus.valueOf(voucherDTO.getStatus()),
                voucherDTO.getArrivalDate(), voucherDTO.getEvictionDate(), null,
                voucherDTO.getIsHot());
    }

    @Test
    void testCreate() {
        when(voucherMapper.toVoucher(voucherDTO)).thenReturn(voucher);
        when(voucherRepository.save(voucher)).thenReturn(voucher);
        when(voucherMapper.toVoucherDTO(voucher)).thenReturn(voucherDTO);
        VoucherDTO result = voucherService.create(voucherDTO);
        assertNotNull("result must not be null",result);
        assertEquals("resulting voucherDTO must match expected result", voucherDTO, result);
    }


    @Test
    void testChangeHotStatus() {
        Voucher voucher = new Voucher(); voucher.setHot(false); voucher.setId(UUID.fromString(voucherDTO.getId()));
        VoucherDTO expectedVoucherDTO = new VoucherDTO(); expectedVoucherDTO.setIsHot(false);
        voucherDTO.setId(voucher.getId().toString());

        String id = voucher.getId().toString();
        when(voucherRepository.findById(UUID.fromString(id))).thenReturn(Optional.of(voucher));
        when(voucherRepository.save(voucher)).thenReturn(voucher);
        when(voucherMapper.toVoucherDTO(voucher)).thenReturn(expectedVoucherDTO);
        VoucherDTO result = voucherService.changeHotStatus(id, voucherDTO);
        assertNotNull("result must not be null", result);
        assertEquals("resulting voucherDTO must match expected result", expectedVoucherDTO, result);
    }
    @Test
    void testChangeCondition() {
        Voucher voucher = new Voucher(); voucher.setStatus(VoucherStatus.REGISTERED); voucher.setId(UUID.fromString(voucherDTO.getId()));
        VoucherDTO expectedVoucherDTO = new VoucherDTO(); expectedVoucherDTO.setStatus("REGISTERED");
        voucherDTO.setId(voucher.getId().toString());

        String id = voucher.getId().toString();
        when(voucherRepository.findById(UUID.fromString(id))).thenReturn(Optional.of(voucher));
        when(voucherRepository.save(voucher)).thenReturn(voucher);
        when(voucherMapper.toVoucherDTO(voucher)).thenReturn(expectedVoucherDTO);
        VoucherDTO result = voucherService.changeCondition(id, voucherDTO);
        assertNotNull("result must not be null", result);
        assertEquals("resulting voucherDTO must match expected result", expectedVoucherDTO, result);
    }

    @Test
    void testOrder() {
        Voucher voucher = new Voucher();
        UserDTO userDTO = new UserDTO();
        User user = new User(); user.setBalance(BigDecimal.valueOf(100.0)); voucher.setPrice(55.0);
        userDTO.setBalance(user.getBalance().doubleValue());
        voucher.setStatus(VoucherStatus.REGISTERED);
        String id = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();
        userDTO.setId(userId); voucherDTO.setId(id);

        when(voucherRepository.findById(UUID.fromString(voucherDTO.getId()))).thenReturn(Optional.of(voucher));
        when(voucherRepository.save(voucher)).thenReturn(voucher);
        when(userService.getUserById(any(UUID.class))).thenReturn(userDTO);
        when(userService.updateUser(userDTO.getUsername(), userDTO)).thenReturn(userDTO);
        when(voucherMapper.toVoucherDTO(any(Voucher.class))).thenReturn(voucherDTO);
        when(userMapper.toUser(userDTO)).thenReturn(user);

        VoucherDTO result = voucherService.order(id, userId);
        assertNotNull("The returned resultDTO should not be null", result);
        assertNotNull("The voucherDTO must have user's id reference", result.getUserId());
        assertEquals("resulting object's ID must match expected ID", voucherDTO.getId(), result.getId());
    }

    @Test
    void testUpdate() {
        Voucher voucher = new Voucher();
        User user = new User(); user.setId(UUID.randomUUID());
        when(voucherMapper.toVoucher(voucherDTO)).thenReturn(voucher);
        when(voucherRepository.save(any(Voucher.class))).thenReturn(voucher);
        when(voucherMapper.toVoucherDTO(voucher)).thenReturn(voucherDTO);
        VoucherDTO result = voucherService.update(voucherDTO.getUserId().toString(), voucherDTO);
        assertNotNull("The returned resultDTO should not be null", result);
        assertNotNull("The voucherDTO must have user's id reference", result.getUserId());
        assertEquals("ID of resulting VoucherDTO must match expected id",voucherDTO.getId(), result.getId());
    }


    private VoucherDTO getTestVoucherDTO(UUID userId, TourType tourType,
                                         String transferType, Double price,
                                         HotelType hotelType) {
        return VoucherDTO.getNewVoucherDTO(UUID.randomUUID().toString(), "Title","Description",
                499.99, TourType.SAFARI.name(),TransferType.JEEPS.name(),HotelType.THREE_STARS.name(),
                VoucherStatus.PAID.name(), LocalDate.of(2024, 7, 15), LocalDate.of(2024, 7, 20),
                UUID.randomUUID(), true);
    }
    private Voucher getTestVoucher(UUID userId, TourType tourType,
                                   String transferType, Double price,
                                   HotelType hotelType) {
        return Voucher.getNewVoucher("Title", userId, price, "Description", TransferType.valueOf(transferType),
                tourType, hotelType, VoucherStatus.PAID, LocalDate.of(2024, 7, 15), LocalDate.of(2024, 7, 20),
                null, true);
    }

    @Test
    void testFindAll() {
        List<Voucher>  list = List.of(voucher, voucher, voucher);
        List<VoucherDTO> expectedList = List.of(voucherDTO, voucherDTO, voucherDTO);
        when(voucherMapper.toVoucherDTO(any(Voucher.class))).thenReturn(voucherDTO);
        when(voucherRepository.findAll()).thenReturn(list);
        List<VoucherDTO> result = voucherService.findAll();
        assertNotNull("resulting voucherDTO list should not be null", result);
        assertEquals("size of resulting list must match expected size", list.size(), result.size());
        for(int i = 0; i < 3; i++) {
            assertEquals("each voucherDTO in resulting list must match appropriate expected voucherDTO", expectedList.get(i), result.get(i));
        }
    }

    @Test
    void testFind() {
        when(voucherRepository.findById(UUID.fromString(voucherDTO.getId()))).thenReturn(Optional.of(voucher));
        when(voucherMapper.toVoucherDTO(voucher)).thenReturn(voucherDTO);
        VoucherDTO result = voucherService.find(voucherDTO.getId());
        assertNotNull( "resulting voucherDTO should not be null", result);
        assertEquals("resulting voucherDTO must match expected result",voucherDTO, result);
    }

    @Test
    void testFindAllByUserId() {
        List<Voucher>  list = List.of(voucher, voucher, voucher);
        List<VoucherDTO> expectedList = List.of(voucherDTO, voucherDTO, voucherDTO);
        when(voucherMapper.toVoucherDTO(any(Voucher.class))).thenReturn(voucherDTO);
        when(voucherRepository.findAllByUserId(voucherDTO.getUserId())).thenReturn(list);
        List<VoucherDTO> result = voucherService.findAllByUserId(voucherDTO.getUserId().toString());
        assertNotNull("resulting voucherDTO list should not be null", result);
        assertEquals("size of resulting list must match expected size", list.size(), result.size());
        for(int i = 0; i < 3; i++) {
            assertEquals("each voucherDTO in resulting list must match appropriate expected voucherDTO", expectedList.get(i), result.get(i));
        }
    }

    @Test
    void testFilter() {
        VouchersFilterRequest vouchersFilterRequest = new VouchersFilterRequest();
        List<Voucher>  list = List.of(voucher, voucher, voucher);
        List<VoucherDTO> expectedList = List.of(voucherDTO, voucherDTO, voucherDTO);
        when(voucherMapper.toVoucherDTO(any(Voucher.class))).thenReturn(voucherDTO);
        when(voucherRepository.filterVouchers(
                null,null,null,null
        )).thenReturn(list);
        List<VoucherDTO> result = voucherService.filter(vouchersFilterRequest);
        assertNotNull("resulting voucherDTO list should not be null", result);
        assertEquals("size of resulting list must match expected size", list.size(), result.size());
        for(int i = 0; i < 3; i++) {
            assertEquals("each voucherDTO in resulting list must match appropriate expected voucherDTO", expectedList.get(i), result.get(i));
        }
    }

}
