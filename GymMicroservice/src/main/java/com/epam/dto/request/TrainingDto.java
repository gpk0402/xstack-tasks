package com.epam.dto.request;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingDto {
	@NotBlank(message = "Trainee User Name is required")
	 private String traineeUsername;
	@NotBlank(message = "Trainer User Name is required")
	 private String trainerUsername;
	@NotBlank(message = "Training Name is required")
	 private String trainingName;
	@NotNull(message = "Training Date is required")
	 private LocalDate date=LocalDate.now();
	@Schema(accessMode = AccessMode.READ_ONLY)
	 private String trainingType;
	@NotNull(message = "Training Duration is required")
	 private int duration;

}
