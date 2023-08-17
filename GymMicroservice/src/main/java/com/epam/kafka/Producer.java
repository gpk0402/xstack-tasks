package com.epam.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.epam.dto.request.TrainingReportDto;
import com.epam.dto.response.NotificationDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Producer {
	
	private final KafkaTemplate<String,TrainingReportDto> kafkaReportTemplate;
	private final KafkaTemplate<String,NotificationDto> kafkaNotificationTemplate;

	
	public void sendGymReport(TrainingReportDto dto)
	{
		kafkaReportTemplate.send("report-topic",dto);
	}
	
	public void sendNotificationLog(NotificationDto dto)
	{
		kafkaNotificationTemplate.send("notification-topic",dto);
	}
}

