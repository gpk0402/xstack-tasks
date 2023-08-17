package com.epam.service;

import com.epam.dto.request.TrainingDto;
import com.epam.dto.request.TrainingReportDto;

public interface TrainingService {
	TrainingReportDto addTraining(TrainingDto trainingDto);

}
