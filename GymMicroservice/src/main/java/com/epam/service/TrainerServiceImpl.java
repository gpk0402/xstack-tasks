
package com.epam.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.epam.dto.request.TrainerDto;
import com.epam.dto.request.TrainerTrainingsList;
import com.epam.dto.request.TrainerUpdateDto;
import com.epam.dto.response.CredentialsDto;
import com.epam.dto.response.NotificationDto;
import com.epam.dto.response.TrainerProfileDto;
import com.epam.dto.response.TrainingDetailsDto;
import com.epam.entity.Trainer;
import com.epam.entity.Training;
import com.epam.entity.TrainingType;
import com.epam.entity.User;
import com.epam.exception.TrainerException;
import com.epam.exception.TrainingTypeException;
import com.epam.kafka.Producer;
import com.epam.repository.TrainerRepository;
import com.epam.repository.TrainingTypeRepository;
import com.epam.repository.UserRepository;
import com.epam.utils.ServiceMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TrainerServiceImpl implements TrainerService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TrainerRepository trainerRepository;
	@Autowired
	private TrainingTypeRepository trainingTypeRepository;
	@Autowired
	private ServiceMapper serviceMapper;
	@Autowired
	private Producer producer;
	@Autowired
	PasswordEncoder encoder;

	static final String TRAINER_EXCEPTION = "Trainer with username not found";

	static final String TRAININGTYPE_EXCEPTION = "Training type with that specialization is not found";

	@Override
	public CredentialsDto addTrainer(TrainerDto trainerDto) {
		log.info("Entered into Create Trainer Method :{}", trainerDto);
		User user = serviceMapper.createUserTrainerProfile(trainerDto);
		String password = user.getPassword();
		user.setPassword(encoder.encode(password));
		userRepository.save(user);
		TrainingType trainingType = trainingTypeRepository.findByTrainingTypeName(trainerDto.getSpecialization())
				.orElseThrow(() -> new TrainingTypeException(TRAININGTYPE_EXCEPTION));
		Trainer trainer = Trainer.builder().trainingType(trainingType).user(user).build();
		trainerRepository.save(trainer);
		CredentialsDto credentialsDto = CredentialsDto.builder().password(password).username(user.getUsername())
				.build();
		NotificationDto dto = serviceMapper.getRegistrationNotification(credentialsDto, user);
		producer.sendNotificationLog(dto);
		log.info("Retriving user credentials");
		return credentialsDto;
	}

	@Override
	public TrainerProfileDto getTrainerProfile(String username) {
		log.info("Entered into get Trainer Profile of {}", username);
		Trainer trainer = findTrainerByUsername(username);
		log.info("Retriving trainer profile");
		return serviceMapper.getTrainerProfile(trainer);
	}

	@Override
	@Transactional
	public TrainerProfileDto updateTrainerProfile(TrainerUpdateDto updateDto) {
		log.info("Entered into update Trainer Profile of {}", updateDto);
		Trainer trainer = findTrainerByUsername(updateDto.getUsername());
		User user = trainer.getUser();
		user.setActive(true);
		user.setEmail(updateDto.getEmail());
		user.setFirstName(updateDto.getFirstName());
		user.setLastName(updateDto.getLastName());
		updateDto.setSpecialization(trainer.getTrainingType().getTrainingTypeName());
		trainer.setUser(user);
		NotificationDto dto = serviceMapper.getTrainerUpdateNotification(updateDto);
		producer.sendNotificationLog(dto);
		log.info("Retrieving Updated trainer Profile");
		return serviceMapper.getTrainerProfile(trainer);
	}

	@Override
	public List<TrainingDetailsDto> getTrainerTrainingsList(TrainerTrainingsList trainerTrainingsList) {
		log.info("Entered into getTrainerTrainingsList");
		Trainer trainer = findTrainerByUsername(trainerTrainingsList.getUsername());
		List<Training> trainingsList = trainerRepository.findTrainingsForTrainer(trainer.getUser().getUsername(),
				trainerTrainingsList.getPeriodFrom(), trainerTrainingsList.getPeriodTo(),
				trainerTrainingsList.getTraineeName());
		log.info("Retrieving training details of trainer");
		return serviceMapper.getTrainingDetailsList(trainingsList);
	}

	private Trainer findTrainerByUsername(String username) {
		return trainerRepository.findByUserUsername(username)
				.orElseThrow(() -> new TrainerException(TRAINER_EXCEPTION + " " + username));
	}
}
