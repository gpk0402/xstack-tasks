//package com.epam.service;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.verify;
//
//import java.util.List;
//
//import com.epam.dto.response.NotificationDto;
//import com.epam.kafka.Producer;
//import org.apache.kafka.clients.producer.KafkaProducer;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.kafka.core.KafkaTemplate;
//
//import com.epam.dto.request.TrainingReportDto;
//
//@ExtendWith(MockitoExtension.class)
//public class KafkaServiceTest {
//
//    @Mock
//    private KafkaTemplate<String, NotificationDto> kafkaNotificationTemplate;
//
//    @Mock
//    private KafkaTemplate<String, TrainingReportDto> kafkaReportTemplate;
//
//    @InjectMocks
//    private Producer producer;
//
//    @Test
//    void testSendNotification() {
//
//        NotificationDto notificationDto = new NotificationDto();
//        producer.sendNotificationLog(notificationDto);
//        verify(kafkaNotificationTemplate).send(eq("notification-topic"), any(NotificationDto.class));
//    }
//
//    @Test
//    void testSendReport() {
//
//        TrainingReportDto reportDto = new TrainingReportDto();
//        producer.sendGymReport(reportDto);
//        verify(kafkaReportTemplate).send(eq("report-topic"), any(TrainingReportDto.class));
//    }
//}
//
