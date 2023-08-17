package com.epam.restapi;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.epam.dto.request.TraineeDto;
import com.epam.dto.request.TraineeTrainingsList;
import com.epam.dto.request.TraineeUpdateDto;
import com.epam.dto.response.CredentialsDto;
import com.epam.dto.response.TraineeProfileDto;
import com.epam.dto.response.TrainerDetailsDto;
import com.epam.dto.response.TrainingDetailsDto;
import com.epam.service.TraineeServiceImpl;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("gym/trainee")
@AllArgsConstructor
public class TraineeController {

	private final TraineeServiceImpl traineeServiceImpl;

	@PostMapping("/registration")
	public ResponseEntity<CredentialsDto> addTrainee(@Valid @RequestBody TraineeDto traineeDto) {
		log.info("Entered into add trainee Method RestController :{}", traineeDto);
		return new ResponseEntity<>(traineeServiceImpl.addTrainee(traineeDto), HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<TraineeProfileDto> getTraineeProfile(@RequestParam String username) {
		log.info("Entered into get trainee profile of {} Method RestController  :{}", username);
		return new ResponseEntity<>(traineeServiceImpl.getTraineeProfile(username), HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<TraineeProfileDto> updateTraineeProfile(
			@Valid @RequestBody TraineeUpdateDto traineeProfileUpdate) {
		log.info("Entered into update trainee profile Method RestController :{}", traineeProfileUpdate);
		return new ResponseEntity<>(traineeServiceImpl.updateTraineeProfile(traineeProfileUpdate), HttpStatus.OK);
	}

	@DeleteMapping
	@ResponseStatus(code = HttpStatus.OK)
	public void deleteTraineeProfile(@RequestParam String username) {
		log.info("Entered into delete trainee profile of {} RestController :{}", username);
		traineeServiceImpl.deleteTrainee(username);
	}

	@PutMapping("/trainers")
	public ResponseEntity<List<TrainerDetailsDto>> updateTraineeTrainers(@RequestParam String username,
			@Valid @RequestBody List<String> trainersList) {
		log.info("Entered into update trainee {} trainers List {} Method RestController :{}", username, trainersList);
		return new ResponseEntity<>(traineeServiceImpl.updateTraineeTrainersList(username, trainersList),
				HttpStatus.OK);
	}

	@GetMapping("/notAssignedtrainers")
	public ResponseEntity<List<TrainerDetailsDto>> getNotAssaignedTrainers(@RequestParam String username) {
		log.info("Entered into getNotAssignedTrainers of {} Method RestController :{}", username);
		return new ResponseEntity<>(traineeServiceImpl.getNotAssignedActiveTrainers(username), HttpStatus.OK);
	}

	@PostMapping("/trainings")
	public ResponseEntity<List<TrainingDetailsDto>> getTraineeTrainings(
			@RequestBody TraineeTrainingsList traineeTrainingsList) {
		log.info("Entered into getTraineeTrainings {}  Method RestController :{}", traineeTrainingsList);
		return new ResponseEntity<>(traineeServiceImpl.getTraineeTrainingsList(traineeTrainingsList), HttpStatus.OK);
	}

}
