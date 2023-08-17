package com.epam.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDto {
	private String username;
	private String firstName;
	private String lastName;
	private boolean status;
	private String email;
	private Map<Long,Map<Long,Map<Long,Map<String,Long>>>> trainingSummary;

}

