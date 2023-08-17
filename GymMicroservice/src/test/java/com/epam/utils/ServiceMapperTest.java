package com.epam.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

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
import com.epam.entity.TrainingType;
import com.epam.entity.User;
@ExtendWith(MockitoExtension.class)
class ServiceMapperTest {

    @InjectMocks
    private ServiceMapper serviceMapper;
    
    @Mock
    UsernameGenerator usernameGenerator;
    
    @Mock
    PasswordGenerator passwordGenerator;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
     void testGetCredentials() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("testpassword");

        CredentialsDto credentials = serviceMapper.getCredentials(user);

        assertEquals("testuser", credentials.getUsername());
        assertEquals("testpassword", credentials.getPassword());
    }
    
    @Test
     void testCreateUserTraineeProfile() {
        TraineeDto traineeDto = new TraineeDto();
        traineeDto.setFirstName("John");
        traineeDto.setLastName("Doe");
        traineeDto.setEmail("john.doe@example.com");
        Mockito.when(usernameGenerator.generateUsername("John","Doe")).thenReturn("JohnDoe123456");
        Mockito.when(passwordGenerator.generatePassword()).thenReturn("A!23SCDS:");
        User user = serviceMapper.createUserTraineeProfile(traineeDto);
        assertNotNull(user);
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertTrue(user.getUsername().startsWith("JohnDoe"));
        assertNotNull(user.getPassword());
        assertEquals("john.doe@example.com", user.getEmail());
        assertNotNull(user.getCreatedDate());
        assertTrue(user.isActive());
    }
    
    @Test
     void testCreateUserTrainerProfile() {
        TrainerDto trainerDto = new TrainerDto();
        trainerDto.setFirstName("John");
        trainerDto.setLastName("Doe");
        trainerDto.setEmail("john.doe@example.com");
        Mockito.when(usernameGenerator.generateUsername("John","Doe")).thenReturn("JohnDoe123456");
        Mockito.when(passwordGenerator.generatePassword()).thenReturn("A!23SCDS:");
        User user = serviceMapper.createUserTrainerProfile(trainerDto);
        assertNotNull(user);
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertTrue(user.getUsername().startsWith("JohnDoe"));
        assertNotNull(user.getPassword());
        assertEquals("john.doe@example.com", user.getEmail());
        assertNotNull(user.getCreatedDate());
        assertTrue(user.isActive());
    }
    
    @Test
     void testCreateTraineeProfile() {
        TraineeDto traineeDto = new TraineeDto();
        traineeDto.setAddress("123 Main St");
        traineeDto.setDateOfBirth(LocalDate.parse("2002-05-25"));
        Trainee trainee = new Trainee();
        trainee.setAddress(traineeDto.getAddress());
        assertNotNull(trainee);
    }
    
    @Test
     void testGetTrainingReport() {
        TrainingDto trainingDto = new TrainingDto();
        trainingDto.setDate(LocalDate.parse("2002-05-25"));
        trainingDto.setDuration(2);

        User trainerUser = mock(User.class);
        when(trainerUser.getFirstName()).thenReturn("John");
        when(trainerUser.getLastName()).thenReturn("Doe");
        when(trainerUser.getUsername()).thenReturn("johndoe");
        when(trainerUser.isActive()).thenReturn(true);
        when(trainerUser.getEmail()).thenReturn("john.doe@example.com");

        Trainer trainer = mock(Trainer.class);
        when(trainer.getUser()).thenReturn(trainerUser);

        ServiceMapper serviceMapper = new ServiceMapper();

        TrainingReportDto trainingReportDto = serviceMapper.getTrainingReport(trainingDto, trainer);

        assertNotNull(trainingReportDto);
        assertEquals(LocalDate.parse("2002-05-25"), trainingReportDto.getDate());
        assertEquals(2, trainingReportDto.getDuration());
        assertEquals("John", trainingReportDto.getTrainerFirstName());
        assertEquals("Doe", trainingReportDto.getTrainerLastName());
        assertEquals("johndoe", trainingReportDto.getTrainerUsername());
        assertTrue(trainingReportDto.isTrainerStatus());
        assertEquals("john.doe@example.com", trainingReportDto.getTrainerEmail());
    }
    
    @Test
     void testGetTraineeProfileDto() {
        User traineeUser = mock(User.class);
        when(traineeUser.getUsername()).thenReturn("testuser");
        when(traineeUser.getFirstName()).thenReturn("Jane");
        when(traineeUser.getLastName()).thenReturn("Smith");
        Trainee trainee = mock(Trainee.class);
        when(trainee.getUser()).thenReturn(traineeUser);
        when(trainee.getAddress()).thenReturn("456 Elm St");
        when(trainee.getDateOfBirth()).thenReturn(LocalDate.parse("2002-05-25"));
        ServiceMapper serviceMapper = new ServiceMapper();
        TraineeProfileDto traineeProfileDto = serviceMapper.getTraineeProfileDto(trainee);
        assertNotNull(traineeProfileDto);
        assertEquals("testuser", traineeProfileDto.getUserName());
        assertEquals("Jane", traineeProfileDto.getFirstName());
        assertEquals("Smith", traineeProfileDto.getLastName());
        assertEquals("456 Elm St", traineeProfileDto.getAddress());
        assertEquals(LocalDate.parse("2002-05-25"), traineeProfileDto.getDateOfBirth());
        assertTrue(traineeProfileDto.isActive());
    }
    
    @Test
     void testGetTrainerProfile() {
        User trainerUser = mock(User.class);
        when(trainerUser.getUsername()).thenReturn("traineruser");
        when(trainerUser.getFirstName()).thenReturn("Michael");
        when(trainerUser.getLastName()).thenReturn("Johnson");
        TrainingType trainingType = mock(TrainingType.class);
        when(trainingType.getTrainingTypeName()).thenReturn("Java Programming");
        Trainer trainer = mock(Trainer.class);
        when(trainer.getUser()).thenReturn(trainerUser);
        when(trainer.getTrainingType()).thenReturn(trainingType);
        ServiceMapper serviceMapper = new ServiceMapper();
        TrainerProfileDto trainerProfileDto = serviceMapper.getTrainerProfile(trainer);
        assertNotNull(trainerProfileDto);
        assertEquals("traineruser", trainerProfileDto.getUserName());
        assertEquals("Michael", trainerProfileDto.getFirstName());
        assertEquals("Johnson", trainerProfileDto.getLastName());
        assertEquals("Java Programming", trainerProfileDto.getSpecialization());
        assertTrue(trainerProfileDto.isActive());
    }
    
    @Test
     void testGetTrainerDetailsList() {
        List<Trainer> trainerList = new ArrayList<>();
        Trainer trainer1 = mock(Trainer.class);
        User user1 = mock(User.class);
        when(user1.getUsername()).thenReturn("trainer1");
        when(user1.getFirstName()).thenReturn("John");
        when(user1.getLastName()).thenReturn("Doe");
        TrainingType trainingType1 = mock(TrainingType.class);
        when(trainingType1.getTrainingTypeName()).thenReturn("Java Programming");
        when(trainer1.getUser()).thenReturn(user1);
        when(trainer1.getTrainingType()).thenReturn(trainingType1);
        trainerList.add(trainer1);
        ServiceMapper serviceMapper = new ServiceMapper();
        List<TrainerDetailsDto> trainerDetailsList = serviceMapper.getTrainerDetailsList(trainerList);
        assertNotNull(trainerDetailsList);
        assertEquals(trainerList.size(), trainerDetailsList.size());
        TrainerDetailsDto trainerDetailsDto = trainerDetailsList.get(0);
        assertEquals("trainer1", trainerDetailsDto.getUserName());
        assertEquals("John", trainerDetailsDto.getFirstName());
        assertEquals("Doe", trainerDetailsDto.getLastName());
        assertEquals("Java Programming", trainerDetailsDto.getSpecialization());
    }
    
    @Test
     void testGetTraineeList() {
        List<Trainee> traineeList = new ArrayList<>();
        Trainee trainee1 = mock(Trainee.class);
        User user1 = mock(User.class);
        when(user1.getUsername()).thenReturn("trainee1");
        when(user1.getFirstName()).thenReturn("Alice");
        when(user1.getLastName()).thenReturn("Johnson");
        when(trainee1.getUser()).thenReturn(user1);
        traineeList.add(trainee1);
        ServiceMapper serviceMapper = new ServiceMapper();
        List<TraineeDetailsDto> traineeDetailsList = serviceMapper.getTraineeList(traineeList);
        assertNotNull(traineeDetailsList);
        assertEquals(traineeList.size(), traineeDetailsList.size());
        TraineeDetailsDto traineeDetailsDto = traineeDetailsList.get(0);
        assertEquals("trainee1", traineeDetailsDto.getUserName());
        assertEquals("Alice", traineeDetailsDto.getFirstName());
        assertEquals("Johnson", traineeDetailsDto.getLastName());
    }
    
    @Test
     void testGetTrainingDetailsList() {
        List<Training> trainingList = new ArrayList<>();
        Training training=new Training();
        Trainer trainer=new Trainer();
        Trainee trainee=new Trainee();
        TrainingType trainingType=new TrainingType();
        trainingType.setTrainingTypeName("dance");
        training.setTrainingType(trainingType);;
        User user=new User();
        user.setUsername("lavanya");
        trainer.setUser(user);
        trainee.setUser(user);
        trainer.setTrainingType(trainingType);;
        training.setDate(LocalDate.parse("2002-05-25"));
        training.setDuration(2);
        trainingList.add(training);
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        ServiceMapper serviceMapper = new ServiceMapper();
        List<TrainingDetailsDto> trainingDetailsList = serviceMapper.getTrainingDetailsList(trainingList);
        assertNotNull(trainingDetailsList);
        assertEquals(trainingList.size(), trainingDetailsList.size());
        TrainingDetailsDto trainingDetailsDto = trainingDetailsList.get(0);
        assertEquals(LocalDate.parse("2002-05-25"), trainingDetailsDto.getDate());
        assertEquals(2, trainingDetailsDto.getDuration());
    }
    
    @Test
     void testGetRegistrationNotification() {
        CredentialsDto credentialsDto = CredentialsDto.builder()
                .username("testuser")
                .password("securepassword")
                .build();
        User user = mock(User.class);
        when(user.getEmail()).thenReturn("test@example.com");
        ServiceMapper serviceMapper = new ServiceMapper();
        NotificationDto notificationDto = serviceMapper.getRegistrationNotification(credentialsDto, user);
        assertNotNull(notificationDto);
        assertEquals("Registration Successfull", notificationDto.getSubject());
        assertEquals(1, notificationDto.getToEmails().size());
        assertEquals("test@example.com", notificationDto.getToEmails().get(0));
        assertEquals(0, notificationDto.getCcEmails().size());
        
        String expectedBody = "Dear User your login credentials are :\n"
                + "Username :testuser\n"
                + "Password :securepassword";
        assertEquals(expectedBody, notificationDto.getBody());
    }
    
    @Test
     void testGetTraineeUpdateNotification() {
        TraineeUpdateDto traineeUpdateDto = TraineeUpdateDto.builder()
                .firstName("Alice")
                .lastName("Johnson")
                .username("alice123")
                .address("456 Elm St")
                .email("alice@example.com")
                .isActive(true)
                .build();
        ServiceMapper serviceMapper = new ServiceMapper();
        NotificationDto notificationDto = serviceMapper.getTraineeUpdateNotification(traineeUpdateDto);
        assertNotNull(notificationDto);
        assertEquals("Trainee details updated Successfully", notificationDto.getSubject());
        assertEquals(1, notificationDto.getToEmails().size());
        assertEquals("alice@example.com", notificationDto.getToEmails().get(0));
        assertEquals(0, notificationDto.getCcEmails().size());
        
        String expectedBody = "First Name :Alice\n"
                + "Last Name : Johnson\n"
                + "User Name :alice123\n"
                + "Address :456 Elm St\n"
                + "Email :alice@example.com\n"
                + "Status:true";
        assertEquals(expectedBody, notificationDto.getBody());
    }
    
    @Test
     void testGetTrainerUpdateNotification() {
        TrainerUpdateDto trainerUpdateDto = TrainerUpdateDto.builder()
                .firstName("Michael")
                .lastName("Smith")
                .username("mikesmith")
                .specialization("Java Programming")
                .email("mike@example.com")
                .isActive(true)
                .build();
        ServiceMapper serviceMapper = new ServiceMapper();
        NotificationDto notificationDto = serviceMapper.getTrainerUpdateNotification(trainerUpdateDto);
        assertNotNull(notificationDto);
        assertEquals("Trainer details Updated Successfully", notificationDto.getSubject());
        assertEquals(1, notificationDto.getToEmails().size());
        assertEquals("mike@example.com", notificationDto.getToEmails().get(0));
        assertEquals(0, notificationDto.getCcEmails().size());
        
        String expectedBody = "First Name :Michael\n"
                + "Last Name : Smith\n"
                + "User Name :mikesmith\n"
                + "Specialization :Java Programming\n"
                + "Email :mike@example.com\n"
                + "Status:true";
        assertEquals(expectedBody, notificationDto.getBody());
    }
    
    @Test
     void testGetTrainingNotification() {
        TrainingDto trainingDto = TrainingDto.builder()
                .traineeUsername("trainee123")
                .trainerUsername("trainer456")
                .trainingName("Java Basics")
                .trainingType("Programming")
                .date(LocalDate.parse("2023-08-15"))
                .duration(2)
                .build();
        List<String> emails = List.of("trainee@example.com", "trainer@example.com");
        ServiceMapper serviceMapper = new ServiceMapper();
        NotificationDto notificationDto = serviceMapper.getTrainingNotification(trainingDto, emails);
        assertNotNull(notificationDto);
        assertEquals("Training added Successfully", notificationDto.getSubject());
        assertEquals(2, notificationDto.getToEmails().size());
        assertEquals("trainee@example.com", notificationDto.getToEmails().get(0));
        assertEquals("trainer@example.com", notificationDto.getToEmails().get(1));
        assertEquals(0, notificationDto.getCcEmails().size());
      
    }
    

    @Test
     void testCreateTrainerProfile() {
        TraineeDto traineeDto = new TraineeDto();
        traineeDto.setAddress("123 Main St");
        traineeDto.setDateOfBirth(LocalDate.parse("1995-01-15"));
        User user = new User(); // Create a mock User object
        Trainee result = serviceMapper.createTraineeProfile(traineeDto, user);
        assertEquals("123 Main St",result.getAddress());
    }


}

