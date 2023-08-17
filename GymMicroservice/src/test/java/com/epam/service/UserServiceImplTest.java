package com.epam.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.epam.dto.request.UpdatePassword;
import com.epam.entity.User;
import com.epam.exception.UserException;
import com.epam.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
 class UserServiceImplTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder encoder;

	@InjectMocks
	private UserServiceImpl userService;

	@Test
	void testChangeLogin() {
		UpdatePassword updatePassword = new UpdatePassword();
		updatePassword.setUserName("testUsername");
		updatePassword.setOldPassword("oldPassword");
		updatePassword.setNewPassword("newPassword");

		User mockUser = new User();
		mockUser.setUsername("testUsername");
		mockUser.setPassword("oldPassword");

		Mockito.when(userRepository.findByUsernameAndPassword("testUsername", "oldPassword"))
				.thenReturn(Optional.of(mockUser));
		Mockito.when(encoder.encode("newPassword")).thenReturn("encodedNewPassword");

		userService.changeLogin(updatePassword);

		Mockito.verify(userRepository, times(1)).findByUsernameAndPassword("testUsername", "oldPassword");
		Mockito.verify(encoder, times(1)).encode("newPassword");
	}

	@Test
	void testChangeLoginUserNotFound() {
		UpdatePassword updatePassword = new UpdatePassword();
		updatePassword.setUserName("testUsername");
		updatePassword.setOldPassword("oldPassword");
		updatePassword.setNewPassword("newPassword");
		when(userRepository.findByUsernameAndPassword("testUsername", "oldPassword")).thenReturn(Optional.empty());
		assertThrows(UserException.class, () -> userService.changeLogin(updatePassword));
	}
}
