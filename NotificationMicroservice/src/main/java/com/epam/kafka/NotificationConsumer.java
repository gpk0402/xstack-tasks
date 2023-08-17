package com.epam.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.epam.dto.response.NotificationDto;
import com.epam.service.NotificationServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NotificationConsumer {
	
	@Autowired
	NotificationServiceImpl impl;
	
	@KafkaListener(topics = "notification-topic",groupId = "notification")
	public void sendNotificationLog(NotificationDto notificationDto)
	{
		log.info("Entered into sendNotificationLog method:"+notificationDto);
		impl.addNewNotificationLog(notificationDto);
	}

}
