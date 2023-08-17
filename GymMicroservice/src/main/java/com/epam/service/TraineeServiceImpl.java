package com.epam.service;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TraineeServiceImpl implements TraineeService {
    private TraineeRepository traineeRepository;
    private UserRepository userRepository;
    private TrainerRepository trainerRepository;
    private ServiceMapper serviceMapper;
    private Producer producer;
    private PasswordEncoder encoder;

    static final String TRAINEE_EXCEPTION = "Trainee with username not found";

    @Override
    public CredentialsDto addTrainee(TraineeDto traineeDto) {
        log.info("Entered into Create Trainee Method :{}", traineeDto);
        User user = serviceMapper.createUserTraineeProfile(traineeDto);
        String password = user.getPassword();
        user.setPassword(encoder.encode(password));
        userRepository.save(user);
        Trainee trainee = serviceMapper.createTraineeProfile(traineeDto, user);
        traineeRepository.save(trainee);
        CredentialsDto credentialsDto = CredentialsDto.builder().password(password).username(user.getUsername())
                .build();
        NotificationDto dto = serviceMapper.getRegistrationNotification(credentialsDto, user);
        producer.sendNotificationLog(dto);
        return credentialsDto;
    }

    @Override
    public TraineeProfileDto getTraineeProfile(String username) {
        log.info("Entered into get Trainee Profile of {}", username);
        Trainee trainee = findTraineeByUsername(username);
        return serviceMapper.getTraineeProfileDto(trainee);
    }

    @Override
    @Transactional
    public TraineeProfileDto updateTraineeProfile(TraineeUpdateDto traineeUpdateDto) {
        log.info("Entered into update Trainee Profile of {}", traineeUpdateDto);
        Trainee trainee = findTraineeByUsername(traineeUpdateDto.getUsername());
        User user = trainee.getUser();
        user.setEmail(traineeUpdateDto.getEmail());
        user.setFirstName(traineeUpdateDto.getFirstName());
        user.setLastName(traineeUpdateDto.getLastName());
        user.setActive(traineeUpdateDto.isActive());
        trainee.setAddress(traineeUpdateDto.getAddress());
        trainee.setDateOfBirth(traineeUpdateDto.getDateOfBirth());
        NotificationDto dto = serviceMapper.getTraineeUpdateNotification(traineeUpdateDto);
        producer.sendNotificationLog(dto);
        log.info("Retrieving Updated trainee Profile");
        return serviceMapper.getTraineeProfileDto(trainee);
    }

    @Override
    @Transactional
    public void deleteTrainee(String userName) {
        log.info("Entered into delete trainee profile of {}", userName);
        Trainee trainee = findTraineeByUsername(userName);
        log.info("Deleting the trainee profile");
        traineeRepository.deleteById(trainee.getId());
    }

    @Override
    @Transactional
    public List<TrainerDetailsDto> updateTraineeTrainersList(String username, List<String> trainerUsernames) {
        log.info("Entered into update Trainee TrainersList of username {}", username);
        Trainee trainee = findTraineeByUsername(username);

        List<Trainer> trainersToAdd = trainerUsernames.stream().map(trainerRepository::findByUserUsername)
                .filter(Optional::isPresent).map(Optional::get)
                .filter(trainer -> !trainee.getTrainerList().contains(trainer)).toList();

        List<Trainer> trainersToRemove = trainee.getTrainerList().stream()
                .filter(trainer -> !trainerUsernames.contains(trainer.getUser().getUsername())).toList();

        trainee.getTrainerList().addAll(trainersToAdd);
        trainee.getTrainerList().removeAll(trainersToRemove);
        log.info("Retrieving trainer profile details after updating");
        return serviceMapper.getTrainerDetailsList(trainee.getTrainerList());
    }

    @Override
    public List<TrainerDetailsDto> getNotAssignedActiveTrainers(String username) {
        log.info("Entered into getNotAssignedActiveTrainers of {}", username);
        Trainee trainee = findTraineeByUsername(username);
        List<Trainer> notAssignedTrainers = trainerRepository.findByTraineeListNotContaining(trainee);
        log.info("Retrieving not assigned active trainers");
        return serviceMapper.getTrainerDetailsList(notAssignedTrainers);
    }

    @Override
    public List<TrainingDetailsDto> getTraineeTrainingsList(TraineeTrainingsList traineeTrainingsList) {
        log.info("Entered into getTraineeTrainingsList");
        Trainee trainee = findTraineeByUsername(traineeTrainingsList.getUsername());
        List<Training> trainingsList = traineeRepository.findTrainingsForTrainee(trainee.getUser().getUsername(),
                traineeTrainingsList.getPeriodFrom(), traineeTrainingsList.getPeriodTo(),
                traineeTrainingsList.getTrainerName(), traineeTrainingsList.getTrainingType());
        log.info("Retrieving training details of trainee");
        return serviceMapper.getTrainingDetailsList(trainingsList);
    }

    private Trainee findTraineeByUsername(String username) {
        return traineeRepository.findByUserUsername(username)
                .orElseThrow(() -> new TraineeException(TRAINEE_EXCEPTION + " " + username));
    }
}
