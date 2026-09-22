package com.example.payment.service;

import com.example.payment.model.SeatAllocationEvent;
import org.springframework.stereotype.Service;

@Service
public class PaymentProcessingService {

    public boolean processPayment(SeatAllocationEvent event) {
        return event.getTotalPrice() > 0;
    }
}
