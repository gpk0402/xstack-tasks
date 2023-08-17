package com.epam.dto.request;

import java.time.LocalDate;

import com.epam.dto.request.TrainingReportDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TrainingReportDto {
	private String trainerUsername;
	private String trainerFirstName;
	private String trainerLastName;
	private boolean trainerStatus;
	private String trainerEmail;
	private int duration;
	private LocalDate date;
}
