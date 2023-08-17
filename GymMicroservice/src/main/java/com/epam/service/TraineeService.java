package com.epam.service;

import java.util.List;

import com.epam.dto.request.TraineeDto;
import com.epam.dto.request.TraineeUpdateDto;
import com.epam.dto.request.TraineeTrainingsList;
import com.epam.dto.response.CredentialsDto;
import com.epam.dto.response.TraineeProfileDto;
import com.epam.dto.response.TrainerDetailsDto;
import com.epam.dto.response.TrainingDetailsDto;

public interface TraineeService {

	CredentialsDto addTrainee(TraineeDto traineeDto);

	TraineeProfileDto getTraineeProfile(String username);

	TraineeProfileDto updateTraineeProfile(TraineeUpdateDto traineeProfileUpdate);

	void deleteTrainee(String userName);

	List<TrainerDetailsDto> updateTraineeTrainersList(String username, List<String> trainerUsernames);

	List<TrainerDetailsDto> getNotAssignedActiveTrainers(String username);
	
	List<TrainingDetailsDto> getTraineeTrainingsList(TraineeTrainingsList traineeTrainingsList); 

}
