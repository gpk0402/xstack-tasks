package com.epam.restapi;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.epam.dto.request.TrainingReportDto;
import com.epam.kafka.ReportConsumer;
import com.epam.service.TrainingReportServiceImpl;
@ExtendWith(MockitoExtension.class)
 class ReportConsumerTest {

    @Mock
    private TrainingReportServiceImpl mockServiceImpl;

    @InjectMocks
    private ReportConsumer reportConsumer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
     void testSaveTrainingReport() {
        TrainingReportDto reportDto = new TrainingReportDto();
        reportConsumer.saveTrainingReport(reportDto);
        verify(mockServiceImpl, times(1)).saveTrainingReport(reportDto);
    }
}
