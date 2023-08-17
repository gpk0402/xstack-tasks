package com.epam.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.dto.request.TrainingDto;
import com.epam.dto.request.TrainingReportDto;
import com.epam.dto.response.NotificationDto;
import com.epam.entity.Trainee;
import com.epam.entity.Trainer;
import com.epam.entity.Training;
import com.epam.exception.TraineeException;
import com.epam.exception.TrainerException;
import com.epam.exception.TrainingException;
import com.epam.kafka.Producer;
import com.epam.repository.TraineeRepository;
import com.epam.repository.TrainerRepository;
import com.epam.repository.TrainingRepository;
import com.epam.utils.ServiceMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TrainingServiceImpl implements TrainingService {
	@Autowired
	private TraineeRepository traineeRepository;
	@Autowired
	private TrainerRepository trainerRepository;
	@Autowired
	private TrainingRepository trainingRepository;
	@Autowired
	private Producer producer;
	@Autowired
	private ServiceMapper serviceMapper;

	static final String TRAINEE_EXCEPTION = "Trainee with username not found";

	static final String TRAINER_EXCEPTION = "Trainer with username not found";

	static final String TRAINING_EXCEPTION = "Training cannot be created as trainee or trainer is not associated with each other";

	@Override
	public TrainingReportDto addTraining(TrainingDto trainingDto) {
		log.info("Entered into add Training method {}", trainingDto);
		Trainee trainee = traineeRepository.findByUserUsername(trainingDto.getTraineeUsername())
				.orElseThrow(() -> new TraineeException(TRAINEE_EXCEPTION + " " + trainingDto.getTraineeUsername()));
		Trainer trainer = trainerRepository.findByUserUsername(trainingDto.getTrainerUsername())
				.orElseThrow(() -> new TrainerException(TRAINER_EXCEPTION + " " + trainingDto.getTrainerUsername()));
		if (!trainee.getTrainerList().contains(trainer)) {
			throw new TrainingException(TRAINING_EXCEPTION);
		}
		Training training = Training.builder().date(trainingDto.getDate()).duration(trainingDto.getDuration())
				.trainingName(trainingDto.getTrainingName()).trainee(trainee).trainer(trainer)
				.trainingType(trainer.getTrainingType()).build();

		TrainingReportDto reportDto = serviceMapper.getTrainingReport(trainingDto, trainer);

		NotificationDto dto = serviceMapper.getTrainingNotification(trainingDto,
				List.of(trainee.getUser().getEmail(), trainer.getUser().getEmail()));

		producer.sendNotificationLog(dto);
		trainingRepository.save(training);
		return reportDto;

	}

}
