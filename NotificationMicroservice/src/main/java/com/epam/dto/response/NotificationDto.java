package com.epam.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
	private String subject;
	private String body;
	private List<String> toEmails;
	private List<String> ccEmails;
}
