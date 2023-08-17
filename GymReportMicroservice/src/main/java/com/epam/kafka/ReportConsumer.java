package com.epam.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.epam.dto.request.TrainingReportDto;
import com.epam.service.TrainingReportServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReportConsumer {
	
	@Autowired
	TrainingReportServiceImpl impl;
	
	@KafkaListener(topics = "report-topic",groupId = "gym-report")
	public void saveTrainingReport(TrainingReportDto reportDto)
	{
		log.info("Entered into saveTrainingReport method:"+reportDto);
		impl.saveTrainingReport(reportDto);
	}

}


