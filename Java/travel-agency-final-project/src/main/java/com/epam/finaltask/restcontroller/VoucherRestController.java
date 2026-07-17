package com.epam.finaltask.restcontroller;

import ch.qos.logback.classic.encoder.JsonEncoder;
import com.epam.finaltask.dto.UserDTO;
import com.epam.finaltask.dto.VoucherDTO;
import com.epam.finaltask.model.*;
import com.epam.finaltask.service.UserService;
import com.epam.finaltask.service.VoucherService;
import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.databind.json.JsonMapper;
import jakarta.servlet.http.HttpSession;
import org.aspectj.weaver.patterns.HasThisTypePatternTriedToSneakInSomeGenericOrParameterizedTypePatternMatchingStuffAnywhereVisitor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/vouchers")
public class VoucherRestController {

    private final VoucherService voucherService;

    private final UserService userService;

    public VoucherRestController(VoucherService voucherService, UserService userService) {
        this.voucherService = voucherService;
        this.userService = userService;
    }

    @GetMapping
    ResponseEntity<Map<String, Object>> getAllVouchers() {
        List<VoucherDTO> voucherDTOList = voucherService.findAll();
        Map<String, Object> response = new HashMap<>();
        response.put("results", voucherDTOList);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority(\"ROLE_ADMIN\")")
    @GetMapping("/{id}")
    ResponseEntity<Map<String, Object>> getVoucher(@PathVariable String id) {
        VoucherDTO voucherDTO = voucherService.find(id);
        Map<String, Object> response = new HashMap<>();
        response.put("results", voucherDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{id}")
    ResponseEntity<Map<String, Object>> getVouchersById(@PathVariable String id) {
        List<VoucherDTO> voucherDTOListById = voucherService.findAllByUserId(id);
        Map<String, Object> response = new HashMap<>();
        response.put("results", voucherDTOListById);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority(\"ROLE_ADMIN\")")
    @PostMapping
    ResponseEntity<Map<String, Object>> createNewVoucher(@RequestBody VoucherDTO voucherDTO) {
        voucherService.create(voucherDTO);
        Map<String, Object> response = new HashMap<>();
        response.put("statusCode", HttpStatus.OK);
        response.put("statusMessage", "Voucher is successfully created");
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @PreAuthorize("hasAuthority(\"ROLE_ADMIN\")")
    @PatchMapping("/{id}")
    ResponseEntity<Map<String, Object>> updateVoucher(@RequestBody VoucherDTO voucherDTO, @PathVariable String id) {
        voucherService.update(id, voucherDTO);
        Map<String, Object> response = new HashMap<>();
        response.put("statusCode", HttpStatus.OK);
        response.put("statusMessage", "Voucher is successfully updated");
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Map<String, Object>> deleteVoucher(@PathVariable String id) {
        voucherService.delete(id);
        Map<String, Object> response = new HashMap<>();
        response.put("statusCode", HttpStatus.OK);
        response.put("statusMessage", "Voucher with Id " + id +" has been deleted");
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @PreAuthorize("hasAnyAuthority(\"ROLE_ADMIN\", \"ROLE_MANAGER\")")
    @PatchMapping("/{id}/status")
    ResponseEntity<Map<String, Object>> changeVoucherStatus(@RequestBody VoucherDTO voucherDTO, @PathVariable String id) {
        voucherService.changeHotStatus(id, voucherDTO);
        Map<String, Object> response = new HashMap<>();
        response.put("statusCode", HttpStatus.OK);
        response.put("statusMessage", "Voucher status is successfully changed");
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }


    @PostMapping("/order/{id}")
    ResponseEntity<Map<String, Object>> orderVoucher(@PathVariable String id, Principal principal) {
        UserDTO userDto = userService.getUserByUsername(principal.getName());
        voucherService.order(id, userDto.getId());
        Map<String, Object> response = new HashMap<>();
        response.put("statusCode", HttpStatus.OK);
        response.put("statusMessage", "Voucher with id " + id + " has been ordered by user with name: " + principal.getName());
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @PreAuthorize("hasAnyAuthority(\"ROLE_ADMIN\", \"ROLE_MANAGER\")")
    @PatchMapping("{id}/condition")
    ResponseEntity<Map<String, Object>> changeVoucherCondition(@RequestBody VoucherDTO voucherDTO, @PathVariable String id) {
        voucherService.changeCondition(id, voucherDTO);
        Map<String, Object> response = new HashMap<>();
        response.put("statusCode", HttpStatus.OK);
        response.put("statusMessage", "Voucher condition is successfully changed");
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/filter")
    ResponseEntity<Map<String, Object>> filterVouchers(@RequestBody VouchersFilterRequest vouchersFilterRequest) {
        List<VoucherDTO> filteredList = voucherService.filter(vouchersFilterRequest);
        Map<String, Object> response = new HashMap<>();
        response.put("results", filteredList);
        return ResponseEntity.ok(response);
    }

}
