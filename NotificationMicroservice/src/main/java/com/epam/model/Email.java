package com.epam.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component("email")
public class Email {
	@Id
	private String id;
	private String fromEmail;
	private List<String> toEmails;
	private List<String> ccEmails;
	private String body;
	private String status;
	private String remarks;
}
