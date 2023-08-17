package com.epam.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.epam.dto.request.TrainingDto;
import com.epam.dto.request.TrainingReportDto;
import com.epam.dto.response.NotificationDto;
import com.epam.entity.Trainee;
import com.epam.entity.Trainer;
import com.epam.entity.Training;
import com.epam.entity.TrainingType;
import com.epam.entity.User;
import com.epam.exception.TraineeException;
import com.epam.exception.TrainerException;
import com.epam.exception.TrainingException;
import com.epam.kafka.Producer;
import com.epam.repository.TraineeRepository;
import com.epam.repository.TrainerRepository;
import com.epam.repository.TrainingRepository;
import com.epam.utils.ServiceMapper;
@ExtendWith(MockitoExtension.class)
 class TrainingServiceImplTest {

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private Producer producer;

    @Mock
    private ServiceMapper serviceMapper;
    
    TrainingDto trainingDto;
    Trainer trainer;
    Trainee trainee;

    @InjectMocks
    private TrainingServiceImpl trainingService;
    
    @BeforeEach
    public void setUp()
    {
    	trainingDto = new TrainingDto();
        trainingDto.setTraineeUsername("traineeUsername");
        trainingDto.setTrainerUsername("trainerUsername");

        trainee = new Trainee();
        trainer = new Trainer();
        User user=User.builder().email("lavanya@gmail.com").build();
        trainee.setUser(user);
        trainer.setUser(user);
        trainee.setTrainerList(List.of(trainer));
        TrainingType trainingType=new TrainingType();
        trainer.setTrainingType(trainingType);
    }

    @Test
    void testAddTraining() {
        Mockito.when(traineeRepository.findByUserUsername("traineeUsername")).thenReturn(Optional.of(trainee));
        Mockito.when(trainerRepository.findByUserUsername("trainerUsername")).thenReturn(Optional.of(trainer));
        Mockito.when(serviceMapper.getTrainingReport(eq(trainingDto), any(Trainer.class))).thenReturn(new TrainingReportDto());
        Mockito.when(serviceMapper.getTrainingNotification(any(TrainingDto.class), anyList())).thenReturn(new NotificationDto());

        trainingService.addTraining(trainingDto);

        Mockito.verify(producer, times(1)).sendNotificationLog(any(NotificationDto.class));
        Mockito.verify(trainingRepository, times(1)).save(any(Training.class));
    }
    
    @Test
    void testAddTrainingWithoutAssociation() {
    	trainee.setTrainerList(List.of());
        Mockito.when(traineeRepository.findByUserUsername("traineeUsername")).thenReturn(Optional.of(trainee));
        Mockito.when(trainerRepository.findByUserUsername("trainerUsername")).thenReturn(Optional.of(trainer));
        assertThrows(TrainingException.class,()->trainingService.addTraining(trainingDto));
    }

    @Test
    void testAddTrainingTraineeNotFound() {
        trainingDto.setTraineeUsername("nonExistentTrainee");
        trainingDto.setTrainerUsername("trainerUsername");
        Mockito.when(traineeRepository.findByUserUsername("nonExistentTrainee")).thenReturn(Optional.empty());
        assertThrows(TraineeException.class,()->trainingService.addTraining(trainingDto));
      
    }
    
    @Test
    void testAddTrainingTrainerNotFound() {
        trainingDto.setTraineeUsername("traineeUsername");
        trainingDto.setTrainerUsername("nonExistentTrainer");
        Mockito.when(traineeRepository.findByUserUsername("traineeUsername")).thenReturn(Optional.of(trainee));
        Mockito.when(trainerRepository.findByUserUsername("nonExistentTrainer")).thenReturn(Optional.empty());
        assertThrows(TrainerException.class,()->trainingService.addTraining(trainingDto));
      
    }

}
