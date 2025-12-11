package com.example.onlinetutors.service;

import com.example.onlinetutors.model.Booking;
import com.example.onlinetutors.util.enumclass.StatusBookingEnum;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public interface BookingService {

    void createBooking(Long parentID, Long tutorId, LocalDate date,
                          LocalTime start, LocalTime end, String note);

    Boolean isAvailableBooking(Long tutorId, LocalDate date,
                              LocalTime start, LocalTime end);

    List<Booking> getBookingsByTutorId(Long tutorId);

    List<Booking> getBookingsByParentId(Long parentId);

    void updateBookingStatus(Long bookingId, StatusBookingEnum status);

    List<Booking> getAllBookings();
}
