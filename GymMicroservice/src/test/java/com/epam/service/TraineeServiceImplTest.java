package com.epam.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.epam.dto.request.TraineeDto;
import com.epam.dto.request.TraineeTrainingsList;
import com.epam.dto.request.TraineeUpdateDto;
import com.epam.dto.response.CredentialsDto;
import com.epam.dto.response.NotificationDto;
import com.epam.dto.response.TraineeProfileDto;
import com.epam.dto.response.TrainerDetailsDto;
import com.epam.dto.response.TrainingDetailsDto;
import com.epam.entity.Trainee;
import com.epam.entity.Trainer;
import com.epam.entity.Training;
import com.epam.entity.User;
import com.epam.exception.TraineeException;
import com.epam.kafka.Producer;
import com.epam.repository.TraineeRepository;
import com.epam.repository.TrainerRepository;
import com.epam.repository.UserRepository;
import com.epam.utils.ServiceMapper;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TraineeServiceImplTest {

	@InjectMocks
	private TraineeServiceImpl traineeService;

	@Mock
	private TraineeRepository traineeRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private TrainerRepository trainerRepository;

	@Mock
	private ServiceMapper serviceMapper;

	@Mock
	private Producer producer;

	@Mock
	private PasswordEncoder encoder;

	Trainee trainee;

	Trainer trainer;
	Trainer trainer1;

	NotificationDto dto;

	TraineeDto traineeDto;

	User user;

	List<Trainer> trainersList;

	List<Trainee> traineesList;

	CredentialsDto credentialsDto;

	TraineeUpdateDto traineeUpdateDto;

	TraineeTrainingsList traineeTrainingsList;

	@BeforeEach
	public void setUp() {
		traineeDto = new TraineeDto();
		trainee = new Trainee();
		traineeDto.setEmail("lavanya@gmail.com");
		traineeDto.setFirstName("muvva");
		traineeDto.setLastName("lavanya");
		traineeDto.setAddress("ponnur");
		traineeDto.setDateOfBirth(LocalDate.parse("2002-05-25"));

		user = new User();
		user.setUsername("lavanya");
		user.setPassword("1234");
		user.setEmail("lavanya1@gmail.com");
		user.setActive(true);

		trainee.setAddress("ponnur");
		trainee.setDateOfBirth(LocalDate.parse("2002-05-25"));
		;
		trainee.setUser(user);

		dto = new NotificationDto().builder().subject("Registration Successfull").toEmails(List.of(user.getEmail()))
				.ccEmails(List.of()).body("Dear User your login credentials are :\n" + "Username :" + "lavanya" + "\n"
						+ "Password :" + "1234")
				.build();

		credentialsDto = new CredentialsDto();
		credentialsDto.setUsername("lavanya");
		credentialsDto.setPassword("1234");

		traineeUpdateDto = new TraineeUpdateDto();
		traineeUpdateDto.setUsername("lavanya");

		traineeTrainingsList = new TraineeTrainingsList();
		traineeTrainingsList.setUsername("lavanya");

		trainer1 = new Trainer();
		trainer1.setUser(user);
		trainer = new Trainer();
		trainer.setUser(user);
		trainersList = new ArrayList<>();
		traineesList = new ArrayList<>();
		traineesList.add(trainee);
		trainersList.add(trainer);
		trainee.setTrainerList(trainersList);
		trainer.setTraineeList(traineesList);

	}

	@Test
	void testAddTrainee() {

		Mockito.when(serviceMapper.createUserTraineeProfile(traineeDto)).thenReturn(user);
		Mockito.when(encoder.encode("1234")).thenReturn("encodedPassword");
		Mockito.when(userRepository.save(any(User.class))).thenReturn(user);
		Mockito.when(serviceMapper.createTraineeProfile(traineeDto, user)).thenReturn(trainee);
		Mockito.when(traineeRepository.save(any(Trainee.class))).thenReturn(new Trainee());
		Mockito.when(serviceMapper.getRegistrationNotification(credentialsDto, user)).thenReturn(dto);
		Mockito.doNothing().when(producer).sendNotificationLog(dto);
		CredentialsDto credentialsDto = traineeService.addTrainee(traineeDto);
		assertNotNull(credentialsDto);
		Mockito.verify(userRepository).save(user);
		Mockito.verify(producer).sendNotificationLog(dto);
	}

	@Test
	void testGetTraineeProfile() {
		String username = "testUsername";
		Mockito.when(traineeRepository.findByUserUsername(username)).thenReturn(Optional.of(trainee));
		Mockito.when(serviceMapper.getTraineeProfileDto(trainee)).thenReturn(new TraineeProfileDto());
		TraineeProfileDto profileDto = traineeService.getTraineeProfile(username);
		assertNotNull(profileDto);
		Mockito.verify(traineeRepository).findByUserUsername(username);
	}

	@Test
	void testGetTraineeProfileWithUsernameNotFound() {
		String username = "testUsername";
		Mockito.when(traineeRepository.findByUserUsername(username)).thenReturn(Optional.empty());
		assertThrows(TraineeException.class, () -> traineeService.getTraineeProfile(username));
		Mockito.verify(traineeRepository).findByUserUsername(username);
	}

	@Test
	void testUpdateTraineeProfile() {
		user.setActive(true);
		user.setEmail(traineeUpdateDto.getEmail());
		user.setFirstName(traineeUpdateDto.getFirstName());
		user.setLastName(traineeUpdateDto.getLastName());
		trainee.setUser(user);
		Mockito.when(traineeRepository.findByUserUsername(traineeUpdateDto.getUsername()))
				.thenReturn(Optional.of(trainee));
		Mockito.when(serviceMapper.getTraineeUpdateNotification(traineeUpdateDto)).thenReturn(new NotificationDto());
		Mockito.when(serviceMapper.getTraineeProfileDto(trainee)).thenReturn(new TraineeProfileDto());
		TraineeProfileDto profileDto = traineeService.updateTraineeProfile(traineeUpdateDto);
		assertNotNull(profileDto);
		Mockito.verify(traineeRepository).findByUserUsername(traineeUpdateDto.getUsername());
		Mockito.verify(producer).sendNotificationLog(any(NotificationDto.class));
	}

	@Test
	void testUpdateTraineeProfileWithTraineeNotFound() {
		user.setActive(true);
		user.setEmail(traineeUpdateDto.getEmail());
		user.setFirstName(traineeUpdateDto.getFirstName());
		user.setLastName(traineeUpdateDto.getLastName());
		trainee.setUser(user);
		Mockito.when(traineeRepository.findByUserUsername(traineeUpdateDto.getUsername())).thenReturn(Optional.empty());
		assertThrows(TraineeException.class, () -> traineeService.updateTraineeProfile(traineeUpdateDto));
	}

	@Test
	void testGetTraineeTrainingsList() {

		List<Training> trainingList = new ArrayList<>();
		Mockito.when(traineeRepository.findByUserUsername(traineeTrainingsList.getUsername()))
				.thenReturn(Optional.of(trainee));
		Mockito.when(traineeRepository.findTrainingsForTrainee(user.getUsername(), traineeTrainingsList.getPeriodFrom(),
				traineeTrainingsList.getPeriodTo(), traineeTrainingsList.getTrainerName(),
				traineeTrainingsList.getTrainingType())).thenReturn(trainingList);
		Mockito.when(serviceMapper.getTrainingDetailsList(trainingList)).thenReturn(new ArrayList<>());
		List<TrainingDetailsDto> detailsDtoList = traineeService.getTraineeTrainingsList(traineeTrainingsList);
		assertNotNull(detailsDtoList);
		Mockito.verify(traineeRepository).findByUserUsername(traineeTrainingsList.getUsername());
		Mockito.verify(serviceMapper).getTrainingDetailsList(trainingList);
	}

	@Test
	void testGetTrainerTrainingsListWithTrainerNameNotFound() {
		Mockito.when(traineeRepository.findByUserUsername(traineeTrainingsList.getUsername()))
				.thenReturn(Optional.empty());
		assertThrows(TraineeException.class, () -> traineeService.getTraineeTrainingsList(traineeTrainingsList));
		Mockito.verify(traineeRepository).findByUserUsername(traineeTrainingsList.getUsername());
	}

	@Test
	void testGetNotAssignedActiveTrainers() {
		String username = "testUsername";
		List<Trainer> notAssignedTrainers = new ArrayList<>();
		when(traineeRepository.findByUserUsername(username)).thenReturn(Optional.of(trainee));
		when(trainerRepository.findByTraineeListNotContaining(trainee)).thenReturn(notAssignedTrainers);
		List<TrainerDetailsDto> expectedTrainerDetails = new ArrayList<>();
		when(serviceMapper.getTrainerDetailsList(notAssignedTrainers)).thenReturn(expectedTrainerDetails);
		List<TrainerDetailsDto> result = traineeService.getNotAssignedActiveTrainers(username);
		assertEquals(expectedTrainerDetails, result);
		verify(traineeRepository, times(1)).findByUserUsername(username);
		verify(trainerRepository, times(1)).findByTraineeListNotContaining(trainee);
		verify(serviceMapper, times(1)).getTrainerDetailsList(notAssignedTrainers);
	}

	@Test
	void testDeleteTrainee() {
		String username = "testUsername";
		Trainee trainee = new Trainee();
		trainee.setId(1);
		when(traineeRepository.findByUserUsername(username)).thenReturn(Optional.of(trainee));
		traineeService.deleteTrainee(username);
		verify(traineeRepository, times(1)).findByUserUsername(username);
		verify(traineeRepository, times(1)).deleteById(trainee.getId());
	}

	@Test
	public void testDeleteTrainee_RuntimeException() {
		// Arrange
		String userName = "testUser";
		when(traineeRepository.findById(Math.toIntExact(anyLong()))).thenReturn(Optional.empty()); // Simulate empty result
		assertThrows(RuntimeException.class, () -> {
			traineeService.deleteTrainee(userName);
		});

		// You can add further assertions based on your business logic
	}

	@Test
	void testUpdateTraineeTrainersList() {
		String username = "testUsername";
		List<String> trainerUsernames = Arrays.asList("trainer1");
		List<Trainer> trainersToAdd = Arrays.asList(trainer1); // Set up trainers to add
		List<Trainer> trainersToRemove = Arrays.asList(trainer); // Set up trainers to remove
		List<Trainer> updatedTrainers = new ArrayList<>(); // Set up updated trainers
		when(traineeRepository.findByUserUsername(username)).thenReturn(Optional.of(trainee));
		when(trainerRepository.findByUserUsername(anyString())).thenReturn(Optional.of(new Trainer()));
		when(serviceMapper.getTrainerDetailsList(anyList())).thenReturn(new ArrayList<>());
		List<TrainerDetailsDto> expectedTrainerDetails = new ArrayList<>();
		when(serviceMapper.getTrainerDetailsList(anyList())).thenReturn(anyList());
		List<TrainerDetailsDto> result = traineeService.updateTraineeTrainersList(username, trainerUsernames);
		assertEquals(expectedTrainerDetails, result);
	}

}
