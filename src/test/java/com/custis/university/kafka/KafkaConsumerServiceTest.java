package com.custis.university.kafka;

import com.custis.university.service.KafkaConsumerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.test.context.EmbeddedKafka;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = { "course_enrollment" })
@EnableKafka
public class KafkaConsumerServiceTest {

    private KafkaConsumerService kafkaConsumerService;

    @BeforeEach
    void setUp() {
        kafkaConsumerService.clearMessages();
    }

    @Test
    public void testListen() {
        String message = "Test message";
        kafkaConsumerService.listen(message);

        assertThat(kafkaConsumerService.getReceivedMessages()).contains(message);
    }
}
