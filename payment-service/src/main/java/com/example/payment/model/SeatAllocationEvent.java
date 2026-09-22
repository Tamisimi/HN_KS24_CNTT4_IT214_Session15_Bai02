package com.example.payment.model;

import lombok.Data;
import java.util.List;

@Data
public class SeatAllocationEvent {
    private String cinemaBookingId;
    private List<String> seatNumbers;
    private long totalPrice;
    private String status;
}
