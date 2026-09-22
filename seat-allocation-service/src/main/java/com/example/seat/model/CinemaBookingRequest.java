package com.example.seat.model;

import lombok.Data;
import java.util.List;

@Data
public class CinemaBookingRequest {
    private String cinemaBookingId;
    private String movieCode;
    private String showTime;
    private List<String> seatNumbers;
    private String customerEmail;
    private long totalPrice;
}
