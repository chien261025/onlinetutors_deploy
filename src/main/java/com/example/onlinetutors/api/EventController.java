package com.example.onlinetutors.api;

import com.example.onlinetutors.model.Event;
import com.example.onlinetutors.model.EventBooking;
import com.example.onlinetutors.model.EventBookingResponseDTO;
import com.example.onlinetutors.model.EventResponseDTO;
import com.example.onlinetutors.model.User;
import com.example.onlinetutors.service.EventService;
import com.example.onlinetutors.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final UserService userService;

    @GetMapping("/events")
    public List<EventResponseDTO> getEvents(HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        Long id = (Long) session.getAttribute("id");

        List<Event> list = this.eventService.getEventsByUserId(id);
        List<EventResponseDTO> events = new ArrayList<>();

        for (Event e : list) {

            events.add(EventResponseDTO.builder()
                    .id(e.getId())
                    .title(e.getTitle())
                    .daysOfWeek(e.getDaysOfWeek()) // ví dụ: "1,3,5"
                    .startTime(e.getStartTime() != null ? e.getStartTime().toString() : null)
                    .endTime(e.getEndTime() != null ? e.getEndTime().toString() : null)
                    .weeks(e.getWeeks())
                    .startRecur(e.getStartRecur() != null ? e.getStartRecur().toString() : null)
                    .color(e.getColor())
                    .courseId(e.getCourse() != null ? e.getCourse().getId() : null)
                    .build());
        }
        return events;
    }

    @GetMapping("/eventBookings")
    public List<EventBookingResponseDTO> getEventBookings(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Long id = (Long) session.getAttribute("id");
        User user = this.userService.getUserById(id);
        if (user.getRole().getName().equals("TUTOR")) {
            List<EventBooking> list = this.eventService.getEventBookingsByTutorId(id);
            List<EventBookingResponseDTO> eventBookings = new ArrayList<>();
            for (EventBooking eb : list) {
                eventBookings.add(EventBookingResponseDTO.builder()
                        .id(eb.getId())
                        .parentId(eb.getParent().getId())
                        .tutorId(eb.getTutor().getId())
                        .day(eb.getDay() != null ? eb.getDay().toString() : null)
                        .startTime(eb.getStartTime() != null ? eb.getStartTime().toString() : null)
                        .endTime(eb.getEndTime() != null ? eb.getEndTime().toString() : null)
                        .build());
            }
            return eventBookings;

        } else if (user.getRole().getName().equals("PARENT")) {
            List<EventBooking> list = this.eventService.getEventBookingsByParentId(id);List<EventBookingResponseDTO> eventBookings = new ArrayList<>();
            for (EventBooking eb : list) {
                eventBookings.add(EventBookingResponseDTO.builder()
                        .id(eb.getId())
                        .parentId(eb.getParent().getId())
                        .tutorId(eb.getTutor().getId())
                        .day(eb.getDay() != null ? eb.getDay().toString() : null)
                        .startTime(eb.getStartTime() != null ? eb.getStartTime().toString() : null)
                        .endTime(eb.getEndTime() != null ? eb.getEndTime().toString() : null)
                        .build());
            }
            return eventBookings;

        }
        return new ArrayList<>();
    }
}
