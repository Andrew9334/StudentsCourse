package com.custis.university.kafka;

import com.custis.university.service.KafkaProducerService;
import org.springframework.kafka.support.SendResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;

public class KafkaProducerServiceTest {

    private KafkaTemplate<String, String> kafkaTemplate;
    private KafkaProducerService kafkaProducerService;

    @BeforeEach
    void setUp() {
        kafkaTemplate = mock(KafkaTemplate.class);
        kafkaProducerService = new KafkaProducerService(kafkaTemplate);
    }

    @Test
    public void testSendMessage() {
        String topic = "course_enrollment";
        String message = "Test message";

        CompletableFuture<SendResult<String, String>> future = mock(CompletableFuture.class);
        when(kafkaTemplate.send(topic, message)).thenReturn(future);

        kafkaProducerService.sendMessage(topic, message);

        verify(kafkaTemplate).send(topic, message);
    }
}
