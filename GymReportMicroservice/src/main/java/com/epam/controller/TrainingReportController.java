package com.epam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.epam.dto.ReportDto;
import com.epam.dto.request.TrainingReportDto;
import com.epam.service.TrainingReportServiceImpl;

@RestController
@RequestMapping("trainingReport")
public class TrainingReportController {
	
	@Autowired
	TrainingReportServiceImpl reportServiceImpl;
	
	@PostMapping
	public ResponseEntity<Void> saveTrainingReport(@RequestBody TrainingReportDto reportDto)
	{
		reportServiceImpl.saveTrainingReport(reportDto);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	
	@GetMapping
	public ResponseEntity<ReportDto> getTrainingReportByUsername(@RequestParam String username)
	{
		return new ResponseEntity<>(reportServiceImpl.getTrainingReportByUsername(username),HttpStatus.OK);
	}
	
	

}

