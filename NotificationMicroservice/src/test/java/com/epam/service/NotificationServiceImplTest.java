package com.epam.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.epam.dto.response.NotificationDto;
import com.epam.model.Email;
import com.epam.repository.NotificationRepository;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

	@Mock
	JavaMailSender javaMailSender;
	@Mock
	NotificationRepository notificationRepository;

	@InjectMocks
	NotificationServiceImpl notificationServiceImpl;

	Email email;
	NotificationDto notificationDto;

	@BeforeEach
	void setUp() {
		notificationDto = new NotificationDto();
		notificationDto.setBody("hi");
		notificationDto.setCcEmails(List.of());
		notificationDto.setSubject("hello");
		notificationDto.setToEmails(List.of());
		email= Email.builder().ccEmails(notificationDto.getCcEmails()).toEmails(notificationDto.getToEmails())
				.body(notificationDto.getBody())
				.build();
	}

	@Test
	void sendNotificationTest() {

		Mockito.doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));
		Mockito.when(notificationRepository.save(any(Email.class))).thenReturn(email);
		notificationServiceImpl.addNewNotificationLog(notificationDto);
		Mockito.verify(javaMailSender,Mockito.times(1)).send(any(SimpleMailMessage.class));
		Mockito.verify(notificationRepository,Mockito.times(1)).save(any(Email.class));

	}
	@Test
	void testSendNotificationExceptionCase() {
		doThrow(new MailSendException("mail sending failed")).when(javaMailSender).send(any(SimpleMailMessage.class));
		notificationServiceImpl.addNewNotificationLog(notificationDto);
		Mockito.verify(javaMailSender,Mockito.times(1)).send(any(SimpleMailMessage.class));
        
	}

}
