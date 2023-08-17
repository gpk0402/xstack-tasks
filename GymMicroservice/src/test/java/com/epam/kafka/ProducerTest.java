package com.epam.kafka;

import com.epam.dto.request.TrainingReportDto;
import com.epam.dto.response.NotificationDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@EnableAutoConfiguration(exclude = KafkaAutoConfiguration.class)
class ProducerTest {
    @Mock
    private KafkaTemplate<String, TrainingReportDto> kafkaReportTemplate;
    @Mock
    private KafkaTemplate<String, NotificationDto> kafkaNotificationTemplate;

    @InjectMocks
    Producer producer;

    @Test
    void testSendGymReport() {
        TrainingReportDto reportDto = new TrainingReportDto();
        Mockito.when(kafkaReportTemplate.send(any(String.class),any(TrainingReportDto.class))).thenReturn(null);
        producer.sendGymReport(reportDto);
    }

    @Test
    void testSendNotificationLog() {
        NotificationDto notificationDto = new NotificationDto();
        Mockito.when(kafkaNotificationTemplate.send(any(String.class),any(NotificationDto.class))).thenReturn(null);
        producer.sendNotificationLog(notificationDto);
    }


}
