package com.epam.service;

import java.util.List;

import com.epam.dto.request.TrainerDto;
import com.epam.dto.request.TrainerTrainingsList;
import com.epam.dto.request.TrainerUpdateDto;
import com.epam.dto.response.CredentialsDto;
import com.epam.dto.response.TrainerProfileDto;
import com.epam.dto.response.TrainingDetailsDto;

public interface TrainerService {
	CredentialsDto addTrainer(TrainerDto trainerDto);

	TrainerProfileDto getTrainerProfile(String username);
	
	TrainerProfileDto updateTrainerProfile(TrainerUpdateDto trainerProfileUpdate);
	
	List<TrainingDetailsDto> getTrainerTrainingsList(TrainerTrainingsList trainerTrainingsList);

}
