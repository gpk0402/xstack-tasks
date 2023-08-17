package com.epam.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.epam.dto.ReportDto;
import com.epam.dto.request.TrainingReportDto;
import com.epam.exception.TrainingReportException;
import com.epam.model.TrainingReport;
import com.epam.repository.TrainingReportRepository;

@ExtendWith(MockitoExtension.class)
class TrainingReportServiceImplTest {

	@Mock
	private TrainingReportRepository mockReportRepository;

	@InjectMocks
	private TrainingReportServiceImpl reportService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testSaveTrainingReport() {
		TrainingReportDto reportDto = new TrainingReportDto();
		reportDto.setTrainerUsername("testTrainer");
		reportDto.setTrainerFirstName("John");
		reportDto.setTrainerLastName("Doe");
		reportDto.setTrainerStatus(true);
		reportDto.setTrainerEmail("john.doe@example.com");
		reportDto.setDate(LocalDate.of(2023, 8, 12));
		reportDto.setDuration(120); 
		TrainingReport mockReport = new TrainingReport();
		when(mockReportRepository.findById("testTrainer")).thenReturn(Optional.empty());
		when(mockReportRepository.save(any(TrainingReport.class))).thenReturn(mockReport);
		reportService.saveTrainingReport(reportDto);
		verify(mockReportRepository, times(1)).findById("testTrainer");
		verify(mockReportRepository, times(1)).save(any(TrainingReport.class));
	}
	
	@Test
	void testGetTrainingReport()
	{
		TrainingReport trainingReport=TrainingReport.builder().email("lavanya@gmail.com").firstName("Lavanya").lastName("muvva").status(true).trainingSummary(new HashMap<>()).build();
		when(mockReportRepository.findById(anyString())).thenReturn(Optional.of(trainingReport));
		ReportDto result=reportService.getTrainingReportByUsername("Lavanya");
		assertNotNull(result);
	}
	
	@Test
	void testGetTrainingReportWithTrainerNameNotPresent()
	{
		when(mockReportRepository.findById(anyString())).thenReturn(Optional.empty());
		assertThrows(TrainingReportException.class,()->reportService.getTrainingReportByUsername("Lavanya"));
	}

}
