package com.epam.service;

import com.epam.dto.response.NotificationDto;

public interface NotificationService {
	
	public void addNewNotificationLog(NotificationDto dto);
}
