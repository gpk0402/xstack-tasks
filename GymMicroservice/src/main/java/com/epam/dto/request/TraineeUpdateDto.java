package com.epam.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
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
public class TraineeUpdateDto {
	
	@NotBlank(message = "Username is required")
	private String username;
	@NotBlank(message = "First Name is required")
	private String firstName;
	@NotBlank(message = "Second Name is required")
	private String lastName;
	@NotNull(message = "Date of birth must not be null")
	private LocalDate dateOfBirth;
	@NotBlank(message = "Address is required")
	private String address;
	@Email(message = "Email should be in the form of abc@gmail.com")
	private String email;
	@AssertTrue(message = "isActive must be true or false")
	private boolean isActive;


}
