package com.epam.restapi;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.epam.dto.request.TrainerDto;
import com.epam.dto.request.TrainerTrainingsList;
import com.epam.dto.request.TrainerUpdateDto;
import com.epam.dto.response.CredentialsDto;
import com.epam.dto.response.TrainerProfileDto;
import com.epam.dto.response.TrainingDetailsDto;
import com.epam.service.TrainerServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(TrainerController.class)
class TrainerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrainerServiceImpl trainerService;

    @InjectMocks
    private TrainerController trainerController;
    
    
    TrainerDto trainerDto;
    
    
    TrainerUpdateDto trainerUpdateDto;
    
    TrainerTrainingsList trainerTrainingsList;

    @BeforeEach
    void setUp() {
    	
    	trainerDto=new TrainerDto();
    	trainerDto.setEmail("lavanyamuvva1@gmail.com");
    	trainerDto.setFirstName("muvva");
    	trainerDto.setLastName("lavanya");
    	trainerDto.setSpecialization("Zumba");
        
    	MockitoAnnotations.openMocks(this);

        
        trainerUpdateDto=new TrainerUpdateDto();
        trainerUpdateDto.setActive(true);
        trainerUpdateDto.setEmail("lavanyamuvva@gmail.com");
        trainerUpdateDto.setFirstName("muvva");
        trainerUpdateDto.setLastName("lavanya");
        trainerUpdateDto.setSpecialization("Zumba");
        trainerUpdateDto.setUsername("lavanya");
        
        trainerTrainingsList = new TrainerTrainingsList();
        trainerTrainingsList.setUsername("lavanya");
    }

    @Test
    void testAddTrainer() throws Exception {
    	
    	CredentialsDto credentialsDto=new CredentialsDto();
        Mockito.when(trainerService.addTrainer(Mockito.any(TrainerDto.class)))
               .thenReturn(credentialsDto);

        mockMvc.perform(post("/gym/trainer/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(trainerDto)))
                .andExpect(status().isCreated());
    }
    
    @Test
    void testGetTrainerProfile() throws Exception {
    	TrainerProfileDto profileDto=new TrainerProfileDto();
        String username = "testUsername";
        Mockito.when(trainerService.getTrainerProfile(Mockito.anyString()))
               .thenReturn(profileDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/gym/trainer")
                .param("username", username)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        
    }
    

    @Test
    void testUpdateTrainerProfile() throws Exception {
        TrainerProfileDto updatedProfileDto = new TrainerProfileDto();

        Mockito.when(trainerService.updateTrainerProfile(Mockito.any(TrainerUpdateDto.class)))
               .thenReturn(updatedProfileDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/gym/trainer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(trainerUpdateDto)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    
    @Test
    void testGetTrainerTrainings() throws Exception {
        List<TrainingDetailsDto> trainingDetailsDtoList = Collections.emptyList();

        Mockito.when(trainerService.getTrainerTrainingsList(Mockito.any(TrainerTrainingsList.class)))
               .thenReturn(trainingDetailsDtoList);

        mockMvc.perform(MockMvcRequestBuilders.post("/gym/trainer/trainings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(trainerTrainingsList)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}








    

