package com.epam.restapi;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.epam.dto.request.TrainingDto;
import com.epam.dto.request.TrainingReportDto;
import com.epam.kafka.Producer;
import com.epam.service.TrainingServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@WebMvcTest(TrainingController.class)
class TrainingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrainingServiceImpl trainingService;

    @MockBean
    private Producer producer;

    @InjectMocks
    private TrainingController trainingController;

    @Test
    void testAddTraining() throws Exception {
    	TrainingDto trainingDto = new TrainingDto();
        trainingDto.setTraineeUsername("traineeUser");
        trainingDto.setTrainerUsername("trainerUser");
        trainingDto.setTrainingName("Training Name");
       trainingDto.setDate(java.time.LocalDate.parse("2001-07-31"));
        trainingDto.setTrainingType("Type");
        trainingDto.setDuration(60);

        TrainingReportDto reportDto = new TrainingReportDto();
        reportDto.setTrainerUsername("trainerUser");
        reportDto.setTrainerFirstName("Trainer");
        reportDto.setTrainerLastName("User");
        reportDto.setTrainerStatus(true);
        reportDto.setTrainerEmail("trainer@example.com");
        reportDto.setDuration(60);
      reportDto.setDate(java.time.LocalDate.parse("2001-07-31"));

        Mockito.when(trainingService.addTraining(trainingDto)).thenReturn(reportDto);

        
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mockMvc.perform(post("/gym/training")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(trainingDto)))
                .andExpect(status().isOk());
    }
}
    


