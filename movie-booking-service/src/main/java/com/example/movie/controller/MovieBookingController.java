package com.example.movie.controller;

import com.example.movie.model.CinemaBookingRequest;
import com.example.movie.service.BookingPublisherService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
public class MovieBookingController {

    private final BookingPublisherService bookingPublisherService;

    public MovieBookingController(BookingPublisherService bookingPublisherService) {
        this.bookingPublisherService = bookingPublisherService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String book(@RequestBody CinemaBookingRequest request) {
        bookingPublisherService.createBooking(request);
        return request.getCinemaBookingId();
    }
}
