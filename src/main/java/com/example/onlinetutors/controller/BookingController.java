package com.example.onlinetutors.controller;


import com.example.onlinetutors.model.Booking;
import com.example.onlinetutors.service.BookingService;
import com.example.onlinetutors.service.UserService;
import com.example.onlinetutors.util.enumclass.StatusBookingEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class BookingController {

    private final UserService userService;
    private final BookingService bookingService;

    @GetMapping("/booking/tutor")
    public String getTutorBookingPage(@RequestParam("id") Long tutorId,
                                      HttpServletRequest request,
                                      Model model
    ){
        HttpSession session = request.getSession(false);
        Long parentId = (Long) session.getAttribute("id");
        model.addAttribute("parentId", parentId);
        model.addAttribute("tutorId", tutorId);
        return "client/booking/bookingTutor";

    }

    @PostMapping("/booking/create")
    public String handleCreateBooking(
            Model model,
            @RequestParam("teacherId") Long tutorId,
            @RequestParam("studentId") Long parentId,
            @RequestParam("bookingDate") LocalDate bookingDate,
            @RequestParam("startTime") LocalTime startTime,
            @RequestParam("endTime") LocalTime endTime,
            @RequestParam("note") String note
    ){
        try {
            this.bookingService.createBooking(parentId, tutorId, bookingDate, startTime, endTime, note);
        } catch ( Exception e) {
            log.error("Error creating booking: " + e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/booking/tutor?id=" + tutorId + "&error=true";
        }
        return "redirect:/home-parent"; // trả về lịch
    }

    @GetMapping("/booking/tutor/list")
    public String getTutorBookingListPage(HttpServletRequest request,
                                        Model model
    ) {
        HttpSession session = request.getSession(false);
        Long tutorId = (Long) session.getAttribute("id");
        String role = (String) session.getAttribute("role");
        model.addAttribute("role", role);
        List<Booking> bookings = this.bookingService.getBookingsByTutorId(tutorId);
        model.addAttribute("bookings", bookings);
        return "client/booking/bookingTutorList";
    }

    @PostMapping("/booking/status/success")
    public String handleBookingStatusSuccess(
            @RequestParam("id") Long bookingId
    ) {
        this.bookingService.updateBookingStatus(bookingId, StatusBookingEnum.CONFIRMED);
        return "redirect:/booking/tutor/list";
    }

    @PostMapping("/booking/status/cancel")
    public String handleBookingStatusCancel(
            @RequestParam("id") Long bookingId
    ) {
        this.bookingService.updateBookingStatus(bookingId, StatusBookingEnum.CANCELLED);
        return "redirect:/booking/tutor/list";
    }

    @GetMapping("/booking/parent/list")
    public String getParentBookingListPage(HttpServletRequest request,
                                          Model model
    ) {
        HttpSession session = request.getSession(false);
        Long parentId = (Long) session.getAttribute("id");
        List<Booking> bookings = this.bookingService.getBookingsByParentId(parentId);
        String role = (String) session.getAttribute("role");
        model.addAttribute("role", role);
        model.addAttribute("bookings", bookings);
        return "client/booking/bookingParentList";
    }

    @GetMapping("/admin/booking")
    public String getAdminBookingListPage(Model model) {
        List<Booking> bookings = this.bookingService.getAllBookings();
        model.addAttribute("bookings", bookings);
        return "client/booking/adminBookingPage";
    }

    @PostMapping("/booking/delete")
    public String handleDeleteEbook(@RequestParam("id") Long bookingId) {
        this.bookingService.updateBookingStatus(bookingId, StatusBookingEnum.DELETED);
        return "redirect:/admin/booking";
    }

}
