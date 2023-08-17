package com.epam.kafka;

import com.epam.dto.response.NotificationDto;
import com.epam.kafka.NotificationConsumer;
import com.epam.service.NotificationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationConsumerTest {

    @Mock
    private NotificationServiceImpl notificationService;

    @InjectMocks
    private NotificationConsumer notificationConsumer;

    @Test
    public void testSendNotificationLog() {
        NotificationDto notificationDto = new NotificationDto();
        notificationConsumer.sendNotificationLog(notificationDto);
        verify(notificationService, times(1)).addNewNotificationLog(notificationDto);
    }
}

