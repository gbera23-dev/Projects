package com.epam.finaltask.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import com.epam.finaltask.dto.UserDTO;
import com.epam.finaltask.dto.VoucherDTO;
import com.epam.finaltask.exception.userExceptions.UserAlreadyExistsException;
import com.epam.finaltask.exception.userExceptions.UserCouldNotBeSavedException;
import com.epam.finaltask.exception.userExceptions.UserNotFoundException;
import com.epam.finaltask.mapper.UserMapper;
import com.epam.finaltask.model.LoginRequest;
import com.epam.finaltask.model.User;
import com.epam.finaltask.model.Voucher;
import com.epam.finaltask.model.VoucherStatus;
import com.epam.finaltask.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final UserMapper userMapper;

	public UserServiceImpl(UserRepository userRepository,
						   UserMapper userMapper) {
		this.userRepository = userRepository;
		this.userMapper = userMapper;
	}

	@Override
	public UserDTO register(UserDTO userDTO) {
		log.info("Registering user with name {}", userDTO.getUsername());
		try {
			userRepository.save(userMapper.toUser(userDTO));
		} catch(Exception e) {
			log.error("Failed to register the user with name {}", userDTO.getUsername());
			throw new UserAlreadyExistsException("User with name " + userDTO.getUsername()+" already exists!");
		}
		log.info("User with name {} registration successful", userDTO.getUsername());
		return userDTO;
	}

	@Override
	public UserDTO updateUser(String username, UserDTO userDTO) {
		log.info("Updating information for user with name {}", username);
		User user = null;
		try {
			user = userRepository.findUserByUsername(username).get();
		} catch(NoSuchElementException e) {
			log.error("Failed to find the user with name {}", username);
			throw new UserNotFoundException("User with name " + username +" does not exist!");
		}
		userDTO.setId(user.getId().toString());
		try {
			userRepository.save(userMapper.toUser(userDTO));
		} catch(Exception e) {
			log.error("Failed to update the user with name {}", username);
			throw new UserCouldNotBeSavedException("Could not save the user with name " + userDTO.getUsername());
		}
		log.info("User update was successful!");
		return userDTO;
	}

	@Override
	@Transactional
	public UserDTO getUserByUsername(String username) {
		log.info("Getting user by username {}", username);
		UserDTO userDTO;
		try {
			userDTO = userMapper.toUserDTO(userRepository.findUserByUsername(username).get());
		} catch(Exception e) {
			log.error("Failed to get the user with name {}", username);
			throw new UserNotFoundException("User with name " + username +" does not exist!");
		}
		log.info("Getting user with username {} was successful!", username);
		return userDTO;
	}

	@Override
	public UserDTO changeAccountStatus(UserDTO userDTO) {
		log.info("Changing the status of the account with name {}", userDTO.getUsername());
		User user = null;
		try {
		   user = userRepository.findById(userMapper.toUser(userDTO).getId()).get();
		} catch(Exception e) {
			log.error("Failed to get user with name {}. No user exists with this id {}", userDTO.getUsername(), userDTO.getId());
			throw new UserNotFoundException("User with name " + userDTO.getUsername() +" does not exist!");
		}
		user.setActive(userDTO.isActive());
		try {
			userRepository.save(user);
		}catch(Exception e) {
			log.error("Failed to save the updated user with name {}", userDTO.getUsername());
			throw new UserCouldNotBeSavedException("Could not save the user with name " + userDTO.getUsername());
		}

		log.info("Successfully changed the status of the account with name {}", userDTO.getUsername());
		return userMapper.toUserDTO(user);
	}

	@Override
	public UserDTO getUserById(UUID id) {
		log.info("Getting user by id {}", id);
		UserDTO userDTO = null;
		User user = null;
		try {
			user = userRepository.findById(id).get();
		} catch(Exception e) {
			log.error("User with the id does not exist or id is null");
			throw new UserNotFoundException("User with id " + id +" does not exist!");
		}
		userDTO = userMapper.toUserDTO(user);
		log.info("Successfully obtained the user with id {}", id);
		return userDTO;
	}

}
