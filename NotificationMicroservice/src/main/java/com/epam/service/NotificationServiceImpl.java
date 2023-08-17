package com.epam.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.epam.dto.response.NotificationDto;
import com.epam.model.Email;
import com.epam.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

	@Autowired
	JavaMailSender javaMailSender;

	@Autowired
	NotificationRepository emailRepository;
	
	@Value("${from.mail}")
	private String fromMail;

	public void addNewNotificationLog(NotificationDto dto) {
		log.info("Entered into addNewNotificationLog Method : {}",dto);
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom(fromMail);
		mailMessage.setTo(dto.getToEmails().toArray(new String[0]));
		mailMessage.setCc(dto.getCcEmails().toArray(new String[0]));
		mailMessage.setSubject(dto.getSubject());
		mailMessage.setText(dto.getBody());

		Email notification = Email.builder().fromEmail(fromMail).toEmails(dto.getToEmails())
				.ccEmails(dto.getCcEmails()).body(dto.getBody()).build();
		try {
			javaMailSender.send(mailMessage);
			notification.setStatus("Mail Sent");
			notification.setRemarks("Mail sent successfully!!!");
		} catch (MailException e) {
			notification.setStatus("Mail failed");
			notification.setRemarks("Failed to send a mail " + e.getMessage());
		}
		log.info("Saving the log");
		emailRepository.save(notification);

	}

}
