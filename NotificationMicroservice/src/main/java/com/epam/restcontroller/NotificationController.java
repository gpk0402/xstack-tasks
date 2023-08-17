package com.epam.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epam.dto.response.NotificationDto;
import com.epam.service.NotificationServiceImpl;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("notification")
@Slf4j
public class NotificationController {
	
	@Autowired
	NotificationServiceImpl notificationServiceImpl;
	

	@PostMapping
	public void addNewNotificationLog(@RequestBody NotificationDto dto)
	{
		log.info("Entered into addNewNotificationLog Controller : {}",dto);
		notificationServiceImpl.addNewNotificationLog(dto);
	}
}
