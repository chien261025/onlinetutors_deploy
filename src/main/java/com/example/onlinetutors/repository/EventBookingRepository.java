package com.example.onlinetutors.repository;

import com.example.onlinetutors.model.EventBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventBookingRepository extends JpaRepository<EventBooking, Long> {

    List<EventBooking> findByTutorId(Long tutorId);

    List<EventBooking> findByParentId(Long parentId);
}
