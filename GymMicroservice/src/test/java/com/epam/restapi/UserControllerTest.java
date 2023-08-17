package com.epam.restapi;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.epam.dto.request.UpdatePassword;
import com.epam.service.UserServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(UserController.class)
 class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserServiceImpl userServiceImpl;

	@InjectMocks
	private UserController userController;

	UpdatePassword updatePassword;

	@BeforeEach
	public void setUp() {
		updatePassword = new UpdatePassword();
		updatePassword.setUserName("lavanya");
		updatePassword.setOldPassword("1234");
		updatePassword.setNewPassword("abcd");
	}

	@Test
	void testChangeLogin() throws JsonProcessingException, Exception {
		Mockito.doNothing().when(userServiceImpl).changeLogin(updatePassword);
		mockMvc.perform(put("/gym/user/updatePassword").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(updatePassword))).andExpect(status().isOk());
	}

	@Test
	void testCreateAssociateWithMethodArgumentInvalidException() throws JsonProcessingException, Exception {
		updatePassword = new UpdatePassword();
		mockMvc.perform(put("/gym/user/updatePassword").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(updatePassword))).andExpect(status().isBadRequest())
				.andReturn();

	}

	@Test
	void testCreateAssociateWithHttpMessageNotReadableException() throws JsonProcessingException, Exception {
		Mockito.doNothing().when(userServiceImpl).changeLogin(updatePassword);
		mockMvc.perform(put("/gym/user/updatePassword").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString("{username:"))).andExpect(status().isBadRequest())
				.andReturn();
	}

}
