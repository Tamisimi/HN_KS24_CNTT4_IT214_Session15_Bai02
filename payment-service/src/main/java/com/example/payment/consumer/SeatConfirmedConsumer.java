package com.example.payment.consumer;

import com.example.payment.model.SeatAllocationEvent;
import com.example.payment.service.PaymentProcessingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class SeatConfirmedConsumer {

    private static final Logger log = LoggerFactory.getLogger(SeatConfirmedConsumer.class);

    private final PaymentProcessingService paymentService;
    private final ObjectMapper objectMapper;

    public SeatConfirmedConsumer(PaymentProcessingService paymentService, ObjectMapper objectMapper) {
        this.paymentService = paymentService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "seat-confirmed-events", groupId = "payment-group")
    public void handleSeatConfirmed(ConsumerRecord<String, String> record) {
        String correlationId = new String(record.headers().lastHeader("correlationId").value());

        log.info("[PaymentService] Processing Payment for {}. CorrelationID: {}",
                record.key(), correlationId);

        try {
            SeatAllocationEvent event = objectMapper.readValue(record.value(), SeatAllocationEvent.class);
            boolean success = paymentService.processPayment(event);

            if (success) {
                log.info("[PaymentService] Payment success: {} VND. CorrelationID: {}",
                        event.getTotalPrice(), correlationId);
            }
        } catch (Exception e) {
            log.error("Error processing payment: {}", e.getMessage());
        }
    }
}
