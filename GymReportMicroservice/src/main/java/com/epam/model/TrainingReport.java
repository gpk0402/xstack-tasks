package com.epam.model;

import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "TrainingReport")
public class TrainingReport {
	@Id
	private String username;
	private String firstName;
	private String lastName;
	private boolean status;
	private String email;
	private Map<Long,Map<Long,Map<Long,Map<String,Long>>>> trainingSummary;
				
}
