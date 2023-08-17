package com.epam.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerUpdateDto {
	@NotBlank(message = "Username is required")
	private String username;
	@NotBlank(message = "First Name is required")
	private String firstName;
	@NotBlank(message = "Second Name is required")
	private String lastName;
	@Email(message = "Email should be in the form of abc@gmail.com")
	private String email;
	@Schema(accessMode = AccessMode.READ_ONLY)
	private String specialization;
	@AssertTrue(message = "isActive must be true or false")
	private boolean isActive;
}