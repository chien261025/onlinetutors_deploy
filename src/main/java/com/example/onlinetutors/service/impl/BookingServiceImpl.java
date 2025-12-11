package com.example.onlinetutors.service.impl;

import com.example.onlinetutors.model.Booking;
import com.example.onlinetutors.model.Event;
import com.example.onlinetutors.model.EventBooking;
import com.example.onlinetutors.model.User;
import com.example.onlinetutors.repository.BookingRepository;
import com.example.onlinetutors.repository.EventBookingRepository;
import com.example.onlinetutors.service.BookingService;
import com.example.onlinetutors.service.UserService;
import com.example.onlinetutors.util.enumclass.StatusBookingEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl  implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final EventBookingRepository eventBookingRepository;

    @Override
    public void createBooking(Long parentID, Long tutorId, LocalDate date, LocalTime start, LocalTime end, String note) {
        if (Boolean.FALSE.equals(isAvailableBooking(tutorId, date, start, end))) {
            throw new RuntimeException("Giáo viên đã có lịch giờ này!");
        }
        User parent = userService.getUserById(parentID);
        User tutor = userService.getUserById(tutorId);
        Booking booking = new Booking();
        booking.setParent(parent);
        booking.setTutor(tutor);
        booking.setBookingDate(date);
        booking.setStartTime(start);
        booking.setEndTime(end);
        booking.setNotes(note);
        booking.setStatusBooking(StatusBookingEnum.PENDING);
        bookingRepository.save(booking);

    }

    @Override
    public Boolean isAvailableBooking(Long tutorId, LocalDate date, LocalTime start, LocalTime end) {
        List<Booking> bookings = bookingRepository.findByTutorIdAndBookingDate(tutorId, date);
        for (Booking booking : bookings) {
            if (start.isBefore(booking.getEndTime()) && end.isAfter(booking.getStartTime())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<Booking> getBookingsByTutorId(Long tutorId) {
        return bookingRepository.findByTutorIdAndStatusBooking(tutorId, StatusBookingEnum.PENDING);
    }

    @Override
    public void updateBookingStatus(Long bookingId, StatusBookingEnum status) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));
        booking.setStatusBooking(status);
        if (status == StatusBookingEnum.CONFIRMED) {
            EventBooking eventBooking = EventBooking.builder()
                    .startTime(booking.getStartTime())
                    .endTime(booking.getEndTime())
                    .day(booking.getBookingDate())
                    .tutor(booking.getTutor())
                    .parent(booking.getParent())
                    .color("#FF5733")
                    .build();
            log.info("Creating event booking for confirmed booking: {}", eventBooking);
            this.eventBookingRepository.save(eventBooking);
        }
        this.bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getBookingsByParentId(Long parentId) {
        return bookingRepository.findByParentIdAndStatusBooking(parentId, StatusBookingEnum.PENDING);
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
}
