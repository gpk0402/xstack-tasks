package com.epam.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdatePassword {
	@NotBlank(message = "Username is required")
	private String userName;
	@NotBlank(message = "Old Password is required")
	private String oldPassword;
	@NotBlank(message = "New Password is required")
	private String newPassword;

}
