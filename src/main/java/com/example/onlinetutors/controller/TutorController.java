package com.example.onlinetutors.controller;

import com.example.onlinetutors.model.Course;
import com.example.onlinetutors.model.Event;
import com.example.onlinetutors.model.Role;
import com.example.onlinetutors.model.User;
import com.example.onlinetutors.service.CourseService;
import com.example.onlinetutors.service.EventService;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class TutorController {

    private final UserService userService;
    private final CourseService courseService;
    private final EventService eventService;

    @GetMapping("/tutor")
    public String getParentDashboard(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        String email = (String) session.getAttribute("email");
        List<Course> courses = this.courseService.handleGetCoursesByEmail(email);
    model.addAttribute("role", "TUTOR");
        model.addAttribute("courses", courses);
        return "client/tutor/homeTutor";
    }

    @GetMapping("/tutor/profile")
    public String getTutorProfile(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        String email = (String) session.getAttribute("email");
        User user = this.userService.getUserByEmail(email);
        List<Course> courses = this.courseService.handleGetCoursesByEmail(email);
        model.addAttribute("tutor", user);
        model.addAttribute("role", "TUTOR");
        model.addAttribute("courses", courses);
        return "client/tutor/profileTutor";
    }


    @GetMapping("/tutor/profile/update")
    public String updateTutorProfile(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        String email = (String) session.getAttribute("email");
        User user = this.userService.getUserByEmail(email);
        model.addAttribute("role", "TUTOR");
        model.addAttribute("tutor", user);
        return "client/tutor/updateProfileTutor";
    }

    @PostMapping("/tutor/update-profile")
    public String postUpdateTutorProfile(@ModelAttribute("tutor") User user,
                                         @RequestParam("imageUser") MultipartFile file) {
        Role role = new Role();
        role.setName("TUTOR");
        user.setRole(role);
        this.userService.handleEditUser(user, file);
        return "redirect:/tutor/profile";
    }

    @GetMapping("/tutor/course-update")
    public String getTutorCourseUpdate(@RequestParam("id") Long courseId,
                                       Model model) {
        Course course = this.courseService.handleGetCourseById(courseId);
        Event event = new Event();
        event.setTitle(course.getNameCourse());
        model.addAttribute("event", event);
        model.addAttribute("course", course);
        model.addAttribute("role", "TUTOR");
        return "client/tutor/updateCourseTutor";
    }

    @GetMapping("/tutor/detailed")
    public String getTutorDetailedPage(@RequestParam("id") Long tutorId,
                                       Model model,
                                        HttpServletRequest request
                                       ) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            String roleName = (String) session.getAttribute("role");
            model.addAttribute("role", roleName);
        }
        User tutor = this.userService.getUserById(tutorId);
        model.addAttribute("tutor", tutor);
        return "client/tutor/tutorDetailed";
    }

}
