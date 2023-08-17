package com.epam.utils;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.dto.request.TraineeDto;
import com.epam.dto.request.TraineeUpdateDto;
import com.epam.dto.request.TrainerDto;
import com.epam.dto.request.TrainerUpdateDto;
import com.epam.dto.request.TrainingDto;
import com.epam.dto.request.TrainingReportDto;
import com.epam.dto.response.CredentialsDto;
import com.epam.dto.response.NotificationDto;
import com.epam.dto.response.TraineeDetailsDto;
import com.epam.dto.response.TraineeProfileDto;
import com.epam.dto.response.TrainerDetailsDto;
import com.epam.dto.response.TrainerProfileDto;
import com.epam.dto.response.TrainingDetailsDto;
import com.epam.entity.Trainee;
import com.epam.entity.Trainer;
import com.epam.entity.Training;
import com.epam.entity.User;

@Service
public class ServiceMapper {

	@Autowired
	UsernameGenerator usernameGenerator;

	@Autowired
	PasswordGenerator passwordGenerator;

	public CredentialsDto getCredentials(User user) {
		return CredentialsDto.builder().username(user.getUsername()).password(user.getPassword()).build();
	}

	public User createUserTraineeProfile(TraineeDto traineeDto) {
		
		return User.builder().firstName(traineeDto.getFirstName()).lastName(traineeDto.getLastName())
				.username(usernameGenerator.generateUsername(traineeDto.getFirstName(), traineeDto.getLastName()))
				.password(passwordGenerator.generatePassword())
				.email(traineeDto.getEmail()).createdDate(LocalDate.now()).isActive(true).build();
	}

	public User createUserTrainerProfile(TrainerDto trainerDto) {
		String username = usernameGenerator.generateUsername(trainerDto.getFirstName(), trainerDto.getLastName());
		String password = passwordGenerator.generatePassword();
		return User.builder().firstName(trainerDto.getFirstName()).lastName(trainerDto.getLastName()).username(username)
				.password(password).email(trainerDto.getEmail()).createdDate(LocalDate.now()).isActive(true).build();
	}
	
	public Trainee createTraineeProfile(TraineeDto traineeDto,User user)
	{
		return Trainee.builder()
	            .address(traineeDto.getAddress())
	            .dateOfBirth(traineeDto.getDateOfBirth())
	            .user(user)
	            .build();
	}
	
	public TrainingReportDto getTrainingReport(TrainingDto trainingDto,Trainer trainer)
	{
		return TrainingReportDto.builder().date(trainingDto.getDate())
				.duration(trainingDto.getDuration()).trainerFirstName(trainer.getUser().getFirstName())
				.trainerLastName(trainer.getUser().getLastName()).trainerUsername(trainer.getUser().getUsername())
				.trainerStatus(trainer.getUser().isActive()).trainerEmail(trainer.getUser().getEmail()).build();
	}

	public TraineeProfileDto getTraineeProfileDto(Trainee trainee) {
		return TraineeProfileDto.builder().userName(trainee.getUser().getUsername())
				.firstName(trainee.getUser().getFirstName()).lastName(trainee.getUser().getLastName())
				.address(trainee.getAddress()).dateOfBirth(trainee.getDateOfBirth()).isActive(true)
				.trainersList(getTrainerDetailsList(trainee.getTrainerList())).build();
	}

	public TrainerProfileDto getTrainerProfile(Trainer trainer) {
		User user = trainer.getUser();
		return TrainerProfileDto.builder().userName(user.getUsername()).firstName(user.getFirstName())
				.lastName(user.getLastName()).specialization(trainer.getTrainingType().getTrainingTypeName())
				.isActive(true).traineesList(getTraineeList(trainer.getTraineeList())).build();
	}

	public List<TrainerDetailsDto> getTrainerDetailsList(List<Trainer> trainerList) {
		return trainerList.stream()
				.map(t -> TrainerDetailsDto.builder().userName(t.getUser().getUsername())
						.firstName(t.getUser().getFirstName()).lastName(t.getUser().getLastName())
						.specialization(t.getTrainingType().getTrainingTypeName()).build())
				.toList();
	}

	public List<TraineeDetailsDto> getTraineeList(List<Trainee> traineeList) {
		return traineeList.stream().map(t -> TraineeDetailsDto.builder().userName(t.getUser().getUsername())
				.firstName(t.getUser().getFirstName()).lastName(t.getUser().getLastName()).build()).toList();
	}

	public List<TrainingDetailsDto> getTrainingDetailsList(List<Training> trainingsList) {
		return trainingsList.stream()
				.map(t -> TrainingDetailsDto.builder().date(t.getDate()).duration(t.getDuration())
						.trainerName(t.getTrainer().getUser().getUsername()).trainingName(t.getTrainingName())
						.traineeName(t.getTrainee().getUser().getUsername())
						.trainingType(t.getTrainingType().getTrainingTypeName()).build())
				.toList();
	}

	public NotificationDto getRegistrationNotification(CredentialsDto credentialsDto,User user) {
		return NotificationDto.builder().subject("Registration Successfull").toEmails(List.of(user.getEmail()))
				.ccEmails(List.of()).body("Dear User your login credentials are :\n" + "Username :" + credentialsDto.getUsername()
						+ "\n" + "Password :" + credentialsDto.getPassword())
				.build();
	}

	public NotificationDto getTraineeUpdateNotification(TraineeUpdateDto dto) {
		return NotificationDto.builder().subject("Trainee details updated Successfully")
				.toEmails(List.of(dto.getEmail())).ccEmails(List.of())
				.body("First Name :" + dto.getFirstName() + "\n" + "Last Name : " + dto.getLastName() + "\n"
						+ "User Name :" + dto.getUsername() + "\n" + "Address :" + dto.getAddress() + "\n" + "Email :"
						+ dto.getEmail() + "\n" + "Status:" + dto.isActive())
				.build();
	}

	public NotificationDto getTrainerUpdateNotification(TrainerUpdateDto dto) {
		return NotificationDto.builder().subject("Trainer details Updated Successfully")
				.toEmails(List.of(dto.getEmail())).ccEmails(List.of())
				.body("First Name :" + dto.getFirstName() + "\n" + "Last Name : " + dto.getLastName() + "\n"
						+ "User Name :" + dto.getUsername() + "\n" + "Specialization :" + dto.getSpecialization() + "\n"
						+ "Email :" + dto.getEmail() + "\n" + "Status:" + dto.isActive())
				.build();
	}

	public NotificationDto getTrainingNotification(TrainingDto trainingDto, List<String> mails) {
		return NotificationDto.builder().subject("Training added Successfully").toEmails(mails).ccEmails(List.of())
				.body("Training Details are :\n" + "Trainee Name :" + trainingDto.getTraineeUsername() + "\n"
						+ "Trainer Name :" + trainingDto.getTrainerUsername() + "\n" + "Training Name :"
						+ trainingDto.getTrainingName() + "\n" + "Training Type :" + trainingDto.getTrainingName()
						+ "\n" + "Training Date :" + trainingDto.getDate() + "\n" + "Training Duration :"
						+ trainingDto.getDuration())
				.build();
	} 

}
