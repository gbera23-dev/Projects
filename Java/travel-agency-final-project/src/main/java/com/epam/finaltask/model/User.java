package com.epam.finaltask.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Table(name = "\"user\"")
@Entity
public class User {
	@Id
    private UUID id;

    private String username;

    private String password;

    private Role role;

	@OneToMany
    private List<Voucher> vouchers;

    private String phoneNumber;

    private BigDecimal balance;

    private boolean active;

    public static User getNewUser(UUID id, String username, Role role, String password,
				List<Voucher> vouchers, String phoneNumber, BigDecimal balance, boolean active) {
		User user = new User();
		user.id = id;
		user.username = username;
		user.role = role;
		user.password = password;
		user.vouchers = vouchers;
		user.phoneNumber = phoneNumber;
		user.balance = balance;
		user.active = active;
		return user;
	}

}