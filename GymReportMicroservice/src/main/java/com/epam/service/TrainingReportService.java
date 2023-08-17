package com.epam.service;

import com.epam.dto.ReportDto;
import com.epam.dto.request.TrainingReportDto;

public interface TrainingReportService {
	public void saveTrainingReport(TrainingReportDto reportDto);

	ReportDto getTrainingReportByUsername(String username);


}
