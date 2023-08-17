package com.epam.restapi;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.epam.controller.TrainingReportController;
import com.epam.dto.ReportDto;
import com.epam.dto.request.TrainingReportDto;
import com.epam.service.TrainingReportServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(TrainingReportController.class)
class TrainingReportControllerTest {
	
	@MockBean 
	TrainingReportServiceImpl trainingReportServiceImpl;
	@Autowired
	MockMvc mockMvc;
	

	@Test
	void testSaveTrainingReport() throws JsonProcessingException, Exception
	{
		TrainingReportDto reportDto=new TrainingReportDto();
		Mockito.doNothing().when(trainingReportServiceImpl).saveTrainingReport(reportDto);
		 mockMvc.perform(post("/trainingReport")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(new ObjectMapper().writeValueAsString(reportDto)))
	                .andReturn();
	}
	
	 @Test
	    void testGetTrainingReportDto() throws Exception {
		 ReportDto reportDto=new ReportDto();
	        String username = "testUsername";
	        Mockito.when(trainingReportServiceImpl.getTrainingReportByUsername(Mockito.anyString()))
	               .thenReturn(reportDto);
	        mockMvc.perform(MockMvcRequestBuilders.get("/trainingReport")
	                .param("username", username)
	                .contentType(MediaType.APPLICATION_JSON))
	                .andExpect(MockMvcResultMatchers.status().isOk());
	        
	    }

}
