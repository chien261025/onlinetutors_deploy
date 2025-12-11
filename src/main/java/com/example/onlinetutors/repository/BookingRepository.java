package com.example.onlinetutors.repository;

import com.example.onlinetutors.model.Booking;
import com.example.onlinetutors.util.enumclass.StatusBookingEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByTutorIdAndStatusBooking(Long tutorId, StatusBookingEnum statusBooking);

    List<Booking> findByParentIdAndStatusBooking(Long parentId, StatusBookingEnum statusBooking);

    List<Booking> findByTutorIdAndBookingDate(Long tutorId, LocalDate date);

}
