package com.epam.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainerDto {
	@NotBlank(message = "Enter valid first name")
	@Size(min=3,max=15,message="First Name should contain only 3 to 15 characters")
	private String firstName;
	@NotBlank(message = "Enter valid second name")
	@Size(min=3,max=15,message="Second Name should contain only 3 to 15 characters")
	private String lastName;
	@Pattern(regexp ="^(?)(fitness|yoga|Zumba|stretching|resistance)$",message="Specialization should be fitness or yoga or Zumba or stretching or resistance")
	private String specialization;
	@Email(message="Email should be in the form of abc@gmail.com")
	private String email;

}
