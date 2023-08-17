package com.epam.dto.response;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class TrainingDetailsDto {
	
	private String trainingName;
	private LocalDate date;
	private String trainingType;
	private int duration;
	private String traineeName;
	private String trainerName;
	

}
