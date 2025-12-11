package com.example.onlinetutors.service;


import com.example.onlinetutors.model.Course;
import com.example.onlinetutors.model.Event;
import com.example.onlinetutors.model.EventBooking;
import com.example.onlinetutors.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EventService {

    List<Event> getAllEvents();
    Event handleCreateEvent(Event event) ;

    void handleCreateUserEvent(User user, Event event);

    List<Event> getEventsByUserId(Long userId);

    List<Event> handleGetEventsByCourseId(Long courseId);

    Event getEventByCourseId(Course course);

    List<EventBooking> getEventBookingsByTutorId(Long tutorId);
    List<EventBooking> getEventBookingsByParentId(Long tutorId);
    void handleUpdateEvent(Event event, Course course);
}
