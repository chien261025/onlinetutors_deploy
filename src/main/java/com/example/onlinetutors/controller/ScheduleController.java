package com.example.onlinetutors.controller;

import com.example.onlinetutors.model.Course;
import com.example.onlinetutors.model.Event;
import com.example.onlinetutors.model.User;
import com.example.onlinetutors.service.CourseService;
import com.example.onlinetutors.service.EventService;
import com.example.onlinetutors.service.impl.MomoService;
import com.example.onlinetutors.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ScheduleController {

    private final UserService userService;
    private final CourseService courseService;
    private final EventService eventService;
    private final MomoService momoService;

    @GetMapping("/payment/momo-qr")
    public String getBuyCoursePage(@RequestParam("amount") String amount,
                                   @RequestParam("note") String note,
                                   @RequestParam("id") Long id,
                                   HttpServletRequest request,
                                   Model model
    ) throws Exception {
        HttpSession session = request.getSession(false);
        if(session != null) {
            String role = (String) session.getAttribute("role");
            model.addAttribute("role", role);
        }
        String paymentUrl = this.momoService.handleMomoPayment(amount, note);
        String qrBase64 = this.momoService.generateQRCode(paymentUrl);
        log.info("Accessing buy course page");
        model.addAttribute("amount", amount);
        model.addAttribute("note", note);
        model.addAttribute("qrBase64", qrBase64);
        model.addAttribute("courseId", id);
        return "client/parent/buyCourse";
    }

    @GetMapping("/parent/course-success")
    public String getCourseSuccessPage(
            Model model,
            @RequestParam("id") Long courseId,
            HttpServletRequest request
    ) {
        Course course = this.courseService.handleGetCourseById(courseId);
        model.addAttribute("course", course);
        log.info("Accessing course success page");
        return "client/parent/courseSuccess";
    }


    @PostMapping("/schedule/create")
    public String postCreateSchedule(
            @ModelAttribute("event") Event event,
            @RequestParam("courseId") Long courseId,
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession(false);
        String email = (String) session.getAttribute("email");
        User tutor = this.userService.getUserByEmail(email);
        Course course = this.courseService.handleGetCourseById(courseId);
        event.setCourse(course);
        this.eventService.handleCreateEvent(event);
        this.eventService.handleCreateUserEvent(tutor, event);
        return "redirect:/tutor";
    }

    @GetMapping("/schedule")
    public String getTutorSchedule(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        String role = (String) session.getAttribute("role");
        model.addAttribute("role", role);
        return "client/scheduleTutor";
    }


    //
    @PostMapping("/schedule/edit")
    public String postAdminEditSchedule(
            @ModelAttribute("event") Event event,
            @RequestParam("courseId") Long courseId
    ) {
        Course course = this.courseService.handleGetCourseById(courseId);
        Event existingEvent = this.eventService.getEventByCourseId(course);
        this.eventService.handleUpdateEvent(existingEvent, course);
        return "redirect:/admin/courses";
    }
}
