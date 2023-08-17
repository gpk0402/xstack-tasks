package com.epam.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TraineeDto {
	@NotBlank(message = "Enter valid first name")
	@Size(min=3,max=15,message="First Name should contain only 3 to 15 characters")
	private String firstName;
	@NotBlank(message = "Enter valid second name")
	@Size(min=3,max=15,message="Second Name should contain only 3 to 15 characters")
	private String lastName;
	private LocalDate dateOfBirth;
	private String address;
	@Email(message="Email should be in the form of abc@gmail.com")
	private String email;

}
