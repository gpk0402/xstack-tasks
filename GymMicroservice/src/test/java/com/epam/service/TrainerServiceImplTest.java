package com.epam.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

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
@ExtendWith(MockitoExtension.class)
class TrainerServiceImplTest {

    @InjectMocks
    private TrainerServiceImpl trainerService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @Mock
    private ServiceMapper serviceMapper;

    @Mock
    private Producer producer;

    @Mock
    private PasswordEncoder encoder;
    
    Trainer trainer;
    
    TrainingType trainingType;
    
    NotificationDto dto;
    
    TrainerDto trainerDto;
    
    User user;
    
    CredentialsDto credentialsDto;
    
    TrainerUpdateDto trainerUpdateDto;
    
    TrainerTrainingsList trainerTrainingsList;

    
    @BeforeEach
    public void setUp()
    {
    	trainerDto = new TrainerDto();
        trainer=new Trainer();
        trainerDto.setEmail("lavanya@gmail.com");
        trainerDto.setFirstName("muvva");
        trainerDto.setLastName("lavanya");
        trainerDto.setSpecialization("Zumba");
        
        user=new User();
        user.setUsername("lavanya");
        user.setPassword("1234");
        user.setEmail("lavanya1@gmail.com");
        user.setActive(true);
        
        trainer.setUser(user);
        
        trainingType = new TrainingType();
        trainingType.setTrainingTypeName("Zumba");
        
        trainer.setTrainingType(trainingType);
        
        dto=new NotificationDto().builder().subject("Registration Successfull").toEmails(List.of(user.getEmail()))
				.ccEmails(List.of()).body("Dear User your login credentials are :\n" + "Username :" + "lavanya"
				+ "\n" + "Password :" + "1234")
		.build();
        
        credentialsDto=new CredentialsDto();
        credentialsDto.setUsername("lavanya");
        credentialsDto.setPassword("1234");
        
        trainerUpdateDto=new TrainerUpdateDto();
        trainerUpdateDto.setUsername("lavanya");
        
        trainerTrainingsList = new TrainerTrainingsList();
        trainerTrainingsList.setUsername("lavanya");

    }
    

    @Test
    void testAddTrainer() {
    	String password=user.getPassword();
    	String encodedPAssword="1234567";
        Mockito.when(serviceMapper.createUserTrainerProfile(trainerDto)).thenReturn(user);
        Mockito.when(encoder.encode(password)).thenReturn(encodedPAssword);
        Mockito.when(trainingTypeRepository.findByTrainingTypeName(trainerDto.getSpecialization())).thenReturn(Optional.of(trainingType));
        Mockito.when(trainerRepository.save(any(Trainer.class))).thenReturn(new Trainer());
        Mockito.when(serviceMapper.getRegistrationNotification(credentialsDto, user)).thenReturn(dto);
        Mockito.doNothing().when(producer).sendNotificationLog(dto);
        CredentialsDto credentialsDto = trainerService.addTrainer(trainerDto);
        assertNotNull(credentialsDto);
        verify(userRepository).save(user);
        verify(producer).sendNotificationLog(dto);
    }
    
    @Test
    void testAddTrainerWithTrainingTypeNotFound() {
    	String password=user.getPassword();
    	String encodedPAssword="1234567";
        Mockito.when(serviceMapper.createUserTrainerProfile(trainerDto)).thenReturn(user);
        Mockito.when(encoder.encode(password)).thenReturn(encodedPAssword);
        Mockito.when(trainingTypeRepository.findByTrainingTypeName(trainerDto.getSpecialization())).thenReturn(Optional.empty());
        assertThrows(TrainingTypeException.class,()->trainerService.addTrainer(trainerDto));
    }

    @Test
    void testGetTrainerProfile() {
        String username = "testUsername";
        Mockito.when(trainerRepository.findByUserUsername(username)).thenReturn(Optional.of(trainer));
        Mockito.when(serviceMapper.getTrainerProfile(trainer)).thenReturn(new TrainerProfileDto());
        TrainerProfileDto profileDto = trainerService.getTrainerProfile(username);
        assertNotNull(profileDto);
        Mockito.verify(trainerRepository).findByUserUsername(username);
    }
    
    @Test
    void testGetTrainerProfileWithUsernameNotFound() {
        String username = "testUsername";
        Mockito.when(trainerRepository.findByUserUsername(username)).thenReturn(Optional.empty());
        assertThrows(TrainerException.class,()->trainerService.getTrainerProfile(username));
        Mockito.verify(trainerRepository).findByUserUsername(username);
    }
    
    @Test
    void testUpdateTrainerProfile() {
    	user.setActive(true);
		user.setEmail(trainerUpdateDto.getEmail());
		user.setFirstName(trainerUpdateDto.getFirstName());
		user.setLastName(trainerUpdateDto.getLastName());
		trainerUpdateDto.setSpecialization(trainer.getTrainingType().getTrainingTypeName());
		trainer.setUser(user);
		Mockito.when(trainerRepository.findByUserUsername(trainerUpdateDto.getUsername())).thenReturn(Optional.of(trainer));
		Mockito.when(serviceMapper.getTrainerUpdateNotification(trainerUpdateDto)).thenReturn(new NotificationDto());
		Mockito.when(serviceMapper.getTrainerProfile(trainer)).thenReturn(new TrainerProfileDto());
        TrainerProfileDto profileDto = trainerService.updateTrainerProfile(trainerUpdateDto);
        assertNotNull(profileDto);
        Mockito.verify(trainerRepository).findByUserUsername(trainerUpdateDto.getUsername());
        Mockito.verify(producer).sendNotificationLog(any(NotificationDto.class));
    }
    
    @Test
    void testUpdateTrainerProfileWithTrainerNotFound() {
    	user.setActive(true);
		user.setEmail(trainerUpdateDto.getEmail());
		user.setFirstName(trainerUpdateDto.getFirstName());
		user.setLastName(trainerUpdateDto.getLastName());
		trainerUpdateDto.setSpecialization(trainer.getTrainingType().getTrainingTypeName());
		trainer.setUser(user);
		Mockito.when(trainerRepository.findByUserUsername(trainerUpdateDto.getUsername())).thenReturn(Optional.empty());
        assertThrows(TrainerException.class,()->trainerService.updateTrainerProfile(trainerUpdateDto));
    }
    
    @Test
    void testGetTrainerTrainingsList() {
        
        List<Training> trainingList = new ArrayList<>();
        Mockito.when(trainerRepository.findByUserUsername(trainerTrainingsList.getUsername())).thenReturn(Optional.of(trainer));
        Mockito.when(trainerRepository.findTrainingsForTrainer(
            user.getUsername(),
            trainerTrainingsList.getPeriodFrom(),
            trainerTrainingsList.getPeriodTo(),
            trainerTrainingsList.getTraineeName()
        )).thenReturn(trainingList);
        Mockito.when(serviceMapper.getTrainingDetailsList(trainingList)).thenReturn(new ArrayList<>());
        List<TrainingDetailsDto> detailsDtoList = trainerService.getTrainerTrainingsList(trainerTrainingsList);
        assertNotNull(detailsDtoList);
        Mockito.verify(trainerRepository).findByUserUsername(trainerTrainingsList.getUsername());
        Mockito.verify(serviceMapper).getTrainingDetailsList(trainingList);
    }
    
    @Test
    void testGetTrainerTrainingsListWithTrainerNameNotFound() {
    	Mockito.when(trainerRepository.findByUserUsername(trainerTrainingsList.getUsername())).thenReturn(Optional.empty());
        assertThrows(TrainerException.class,()->trainerService.getTrainerTrainingsList(trainerTrainingsList));
        Mockito.verify(trainerRepository).findByUserUsername(trainerTrainingsList.getUsername());
    }

    

}
