package com.epam.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class Topic {
	
	public NewTopic getReportTopic()
	{
		return TopicBuilder.name("report-topic").build();
	}
	
	public NewTopic getNotificationTopic()
	{
		return TopicBuilder.name("notification-topic").build();
	}

}
