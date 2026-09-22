package com.example.seat.service;

import com.example.seat.model.CinemaBookingRequest;
import org.springframework.stereotype.Service;

@Service
public class SeatAllocationService {

    public String reserveSeats(CinemaBookingRequest request) {
        return String.join(", ", request.getSeatNumbers());
    }
}
