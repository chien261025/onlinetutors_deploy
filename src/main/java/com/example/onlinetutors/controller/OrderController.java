package com.example.onlinetutors.controller;

import com.example.onlinetutors.model.Course;
import com.example.onlinetutors.model.Event;
import com.example.onlinetutors.model.User;
import com.example.onlinetutors.service.CourseService;
import com.example.onlinetutors.service.EventService;
import com.example.onlinetutors.service.OrderService;
import com.example.onlinetutors.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final CourseService courseService;
    private final UserService userService;
    private final OrderService orderService;
    private final EventService eventService;

    @GetMapping("/order/success")
    public String orderSuccess(
            Model model,
            @RequestParam("id") Long courseId,
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession(false);
        if(session != null) {
            String role = (String) session.getAttribute("role");
            model.addAttribute("role", role);
        }
        String email = (String) session.getAttribute("email");
        User user = this.userService.getUserByEmail(email);
        Course course = this.courseService.handleGetCourseById(courseId);

        List<Event> events = this.eventService.handleGetEventsByCourseId(courseId);
        for (Event e : events) {
            this.eventService.handleCreateUserEvent(user, e);

        }
        this.orderService.handleCreateOrder(course, user);
        model.addAttribute("course", course);
        log.info("Order success page requested");
        return "client/parent/success";
    }


}
