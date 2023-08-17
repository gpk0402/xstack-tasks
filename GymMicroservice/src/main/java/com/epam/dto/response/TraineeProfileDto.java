package com.epam.dto.response;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class TraineeProfileDto {
	private String userName;
	private String firstName;
	private String lastName;
	private LocalDate dateOfBirth;
	private String address;
	private boolean isActive;
	private List<TrainerDetailsDto> trainersList;

}
