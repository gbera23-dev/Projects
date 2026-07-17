package com.epam.finaltask.dto;

import java.util.List;

import com.epam.finaltask.model.Voucher;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UserDTO {

	private String id;
	@Size(min = 3, max = 20, message = "username must not be too large or too small")
	private String username;
	@Size(min = 8, message = "password must be at least eight characters")
	private String password;

	private String role;

	private List<VoucherDTO> vouchers;
	@Pattern(regexp = "\\d{9}", message = "Phone number must be exactly 9 digits")
	private String phoneNumber;
	@DecimalMin(value = "0.0", message = "Balance must be a positive number")
	private Double balance;

	private boolean active;

    public static UserDTO getNewUserDTO(String id, String username, String password, String role,
				   List<VoucherDTO> vouchers, String phoneNumber, Double balance, boolean active) {
		UserDTO userDTO = new UserDTO();
		userDTO.id = id;
		userDTO.username = username;
		userDTO.password = password;
		userDTO.role = role;
		userDTO.vouchers = vouchers;
		userDTO.phoneNumber = phoneNumber;
		userDTO.balance = balance;
		userDTO.active = active;
		return userDTO;
	}

}
