package com.epam.restapi;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.epam.dto.request.TrainingDto;
import com.epam.dto.request.TrainingReportDto;
import com.epam.kafka.Producer;
import com.epam.service.TrainingServiceImpl;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("gym/training")
@Slf4j
@AllArgsConstructor
public class TrainingController {

	private final TrainingServiceImpl trainingServiceImpl;

	private final Producer producer;

	@PostMapping
	@ResponseStatus(code = HttpStatus.OK)
	public void addTraining(@Valid @RequestBody TrainingDto trainingDto) {
		log.info("Entered into add Training Method RestController :{}", trainingDto);
		TrainingReportDto reportDto = trainingServiceImpl.addTraining(trainingDto);
		producer.sendGymReport(reportDto);
	}

}
