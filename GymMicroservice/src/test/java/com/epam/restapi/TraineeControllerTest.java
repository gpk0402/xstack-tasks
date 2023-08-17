package com.epam.restapi;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.epam.dto.request.TraineeDto;
import com.epam.dto.request.TraineeTrainingsList;
import com.epam.dto.request.TraineeUpdateDto;
import com.epam.dto.response.CredentialsDto;
import com.epam.dto.response.TraineeProfileDto;
import com.epam.dto.response.TrainerDetailsDto;
import com.epam.dto.response.TrainingDetailsDto;
import com.epam.service.TraineeServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@WebMvcTest(TraineeController.class)
class TraineeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TraineeServiceImpl traineeService;

	@InjectMocks
	private TraineeController traineeController;

	TraineeDto traineeDto;
	
	TraineeUpdateDto traineeUpdateDto;
	
	TraineeTrainingsList traineeTrainingsList;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		traineeDto = new TraineeDto();
		traineeDto.setAddress("ponnur");
		traineeDto.setEmail("lavanya1@gmail.com");
		traineeDto.setFirstName("lavanya");
		traineeDto.setLastName("muvva");
		traineeDto.setDateOfBirth(LocalDate.parse("2023-05-25"));
		
		traineeUpdateDto=new TraineeUpdateDto();
		traineeUpdateDto.setActive(true);
		traineeUpdateDto.setAddress("ponnur");
		traineeUpdateDto.setEmail("lavanyamuvva1@gmail.com");
		traineeUpdateDto.setFirstName("lavanya");
		traineeUpdateDto.setLastName("muvva");
		traineeUpdateDto.setDateOfBirth(LocalDate.parse("2023-05-25"));
		traineeUpdateDto.setUsername("lavanya");
		
		traineeTrainingsList = new TraineeTrainingsList();
		traineeTrainingsList.setUsername("lavanya");
	}

	@Test
	void testAddTrainee() throws Exception {

		CredentialsDto credentialsDto = new CredentialsDto();
		Mockito.when(traineeService.addTrainee(any(TraineeDto.class))).thenReturn(credentialsDto);
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());  
		mockMvc.perform(post("/gym/trainee/registration").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(traineeDto))).andExpect(status().isCreated());
		
	}
	
	@Test
    void testGetTraineeProfile() throws Exception {
    	TraineeProfileDto profileDto=new TraineeProfileDto();
        String username = "testUsername";
        Mockito.when(traineeService.getTraineeProfile(Mockito.anyString()))
               .thenReturn(profileDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/gym/trainee")
                .param("username", username)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        
    }
	
	@Test
    void testUpdateTrainerProfile() throws Exception {
        TraineeProfileDto updatedProfileDto = new TraineeProfileDto();
        ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        Mockito.when(traineeService.updateTraineeProfile(any(TraineeUpdateDto.class)))
               .thenReturn(updatedProfileDto);

        mockMvc.perform(put("/gym/trainee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(traineeUpdateDto)))
                .andExpect(status().isOk());
    }
	
	@Test
    void testDeleteTraineeProfile() throws Exception {
        String username = "testUsername";

        mockMvc.perform(delete("/gym/trainee")
                .param("username", username)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(traineeService, Mockito.times(1)).deleteTrainee(username);
    }
	
	@Test
    void testUpdateTraineeTrainers() throws Exception {
        String username = "testUsername";
        List<String> trainersList = List.of("lavanya");
        List<TrainerDetailsDto> trainerDetailsDtoList = Collections.emptyList();
        Mockito.when(traineeService.updateTraineeTrainersList(Mockito.anyString(), Mockito.anyList()))
               .thenReturn(trainerDetailsDtoList);
        mockMvc.perform(put("/gym/trainee/trainers")
                .param("username", username)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(trainersList)))
                .andExpect(status().isOk());
    }
	
	@Test
    void testGetNotAssignedTrainers() throws Exception {
        String username = "testUsername";

        List<TrainerDetailsDto> trainerDetailsDtoList = Collections.emptyList();
        Mockito.when(traineeService.getNotAssignedActiveTrainers(Mockito.anyString()))
               .thenReturn(trainerDetailsDtoList);

        mockMvc.perform(get("/gym/trainee/notAssignedtrainers")
                .param("username", username)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
	
	@Test
    void testGetTraineeTrainings() throws Exception {
        
        List<TrainingDetailsDto> trainingDetailsDtoList = Collections.emptyList();
        Mockito.when(traineeService.getTraineeTrainingsList(any(TraineeTrainingsList.class)))
               .thenReturn(trainingDetailsDtoList);

        mockMvc.perform(post("/gym/trainee/trainings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(traineeTrainingsList)))
                .andExpect(status().isOk());
    }



}
