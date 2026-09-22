package com.example.movie.service;

import com.example.movie.model.CinemaBookingRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
public class BookingPublisherService {

    private static final Logger log = LoggerFactory.getLogger(BookingPublisherService.class);
    private static final String TOPIC = "booking-events";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public BookingPublisherService(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void createBooking(CinemaBookingRequest request) {
        // Bước 1: sinh Correlation ID tại điểm đầu vào
        String correlationId = UUID.randomUUID().toString();
        log.info("[MovieBookingService] Created booking {}. CorrelationID: {}",
                request.getCinemaBookingId(), correlationId);

        try {
            String payload = objectMapper.writeValueAsString(request);

            ProducerRecord<String, String> record = new ProducerRecord<>(
                    TOPIC,
                    request.getCinemaBookingId(),
                    payload
            );

            // Bước 2: gắn correlationId vào HEADER — không đặt trong payload
            record.headers().add("correlationId", correlationId.getBytes(StandardCharsets.UTF_8));

            kafkaTemplate.send(record);
        } catch (JsonProcessingException e) {
            log.error("Error serializing booking request", e);
        }
    }
}
