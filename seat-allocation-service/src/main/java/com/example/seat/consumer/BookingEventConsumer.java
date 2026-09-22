package com.example.seat.consumer;

import com.example.seat.model.CinemaBookingRequest;
import com.example.seat.producer.SeatConfirmedProducer;
import com.example.seat.service.SeatAllocationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class BookingEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(BookingEventConsumer.class);

    private final SeatAllocationService seatService;
    private final SeatConfirmedProducer seatConfirmedProducer;
    private final ObjectMapper objectMapper;

    public BookingEventConsumer(SeatAllocationService seatService,
                                SeatConfirmedProducer seatConfirmedProducer,
                                ObjectMapper objectMapper) {
        this.seatService = seatService;
        this.seatConfirmedProducer = seatConfirmedProducer;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "booking-events", groupId = "seat-group")
    public void handleBooking(ConsumerRecord<String, String> record) {
        // Trích xuất correlationId từ HEADER
        String correlationId = new String(record.headers().lastHeader("correlationId").value());

        log.info("[SeatAllocationService] Received SeatRequest for {}. CorrelationID: {}",
                record.key(), correlationId);

        try {
            CinemaBookingRequest request = objectMapper.readValue(record.value(), CinemaBookingRequest.class);
            String seatResult = seatService.reserveSeats(request);

            log.info("[SeatAllocationService] Seat reserved: {}. CorrelationID: {}",
                    seatResult, correlationId);

            seatConfirmedProducer.publishSeatConfirmed(request, correlationId);
        } catch (Exception e) {
            log.error("Error processing booking: {}", e.getMessage());
        }
    }
}
