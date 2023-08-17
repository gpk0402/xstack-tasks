package com.epam.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.epam.dto.response.NotificationDto;
import com.epam.restcontroller.NotificationController;
import com.epam.service.NotificationServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {
	
	@MockBean
	NotificationServiceImpl notificationServiceImpl;
	
	@Autowired
	MockMvc mockMvc;
	
	@Test
	void testSendNotification() throws JsonProcessingException, Exception
	{
		NotificationDto notificationDto=new NotificationDto();
		Mockito.doNothing().when(notificationServiceImpl).addNewNotificationLog(notificationDto);
		 mockMvc.perform(post("/notification")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(new ObjectMapper().writeValueAsString(notificationDto)))
	                .andReturn();
	}

}
