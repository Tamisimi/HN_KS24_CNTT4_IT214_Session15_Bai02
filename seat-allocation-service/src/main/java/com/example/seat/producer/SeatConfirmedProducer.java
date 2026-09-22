package com.example.seat.producer;

import com.example.seat.model.CinemaBookingRequest;
import com.example.seat.model.SeatAllocationEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class SeatConfirmedProducer {

    private static final Logger log = LoggerFactory.getLogger(SeatConfirmedProducer.class);
    private static final String TOPIC = "seat-confirmed-events";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public SeatConfirmedProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publishSeatConfirmed(CinemaBookingRequest request, String correlationId) {
        try {
            SeatAllocationEvent event = new SeatAllocationEvent(
                    request.getCinemaBookingId(),
                    request.getSeatNumbers(),
                    request.getTotalPrice(),
                    "SEAT_CONFIRMED"
            );
            String payload = objectMapper.writeValueAsString(event);

            ProducerRecord<String, String> record = new ProducerRecord<>(
                    TOPIC, request.getCinemaBookingId(), payload);

            // Forward cùng correlationId trong HEADER
            record.headers().add("correlationId", correlationId.getBytes(StandardCharsets.UTF_8));
            kafkaTemplate.send(record);

            log.info("[SeatAllocationService] Published seat-confirmed. CorrelationID: {}", correlationId);
        } catch (JsonProcessingException e) {
            log.error("Serialize seat event error", e);
        }
    }
}
