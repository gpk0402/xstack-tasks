package com.epam.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.dto.ReportDto;
import com.epam.dto.request.TrainingReportDto;
import com.epam.exception.TrainingReportException;
import com.epam.model.TrainingReport;
import com.epam.repository.TrainingReportRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TrainingReportServiceImpl implements TrainingReportService {

	@Autowired
	TrainingReportRepository reportRepository;

	@Override
	public void saveTrainingReport(TrainingReportDto reportDto) {
		log.info("Entered into save Training Report method :{}",reportDto);
		TrainingReport report = reportRepository.findById(reportDto.getTrainerUsername())
				.orElseGet(() -> TrainingReport.builder().firstName(reportDto.getTrainerFirstName())
						.lastName(reportDto.getTrainerLastName()).username(reportDto.getTrainerUsername())
						.status(reportDto.isTrainerStatus()).email(reportDto.getTrainerEmail()).build());
		if (report.getTrainingSummary() == null) {
			report.setTrainingSummary(new HashMap<>());
		}
		long year = reportDto.getDate().getYear();
		long month = reportDto.getDate().getMonthValue();
		long day = reportDto.getDate().getDayOfMonth();
		long duration = reportDto.getDuration();

		report.getTrainingSummary().computeIfAbsent(year, k -> new HashMap<>())
				.computeIfAbsent(month, k -> new HashMap<>()).computeIfAbsent(day, k -> new HashMap<>())
				.put(reportDto.getDate().toString(), duration);
		log.info("Saving the report");
		reportRepository.save(report);

	}
	
	@Override
	public ReportDto getTrainingReportByUsername(String username){
		log.info("Entered into getTrainingReportByUsername :{} method ",username);
		return reportRepository.findById(username).map(report->
			ReportDto.builder().email(report.getEmail()).firstName(report.getFirstName())
					.lastName(report.getLastName()).username(report.getUsername()).status(report.isStatus())
					.trainingSummary(report.getTrainingSummary()).build()
		).orElseThrow(()->new TrainingReportException("Training report with username doesnt exist"));
	}


}
