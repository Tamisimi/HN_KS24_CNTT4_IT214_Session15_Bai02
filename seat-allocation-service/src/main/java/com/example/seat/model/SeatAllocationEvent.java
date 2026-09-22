package com.example.seat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatAllocationEvent {
    private String cinemaBookingId;
    private List<String> seatNumbers;
    private long totalPrice;
    private String status;
}
