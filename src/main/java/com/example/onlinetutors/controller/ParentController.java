package com.example.onlinetutors.controller;

import com.example.onlinetutors.model.Comment;
import com.example.onlinetutors.model.Course;
import com.example.onlinetutors.model.Role;
import com.example.onlinetutors.model.User;
import com.example.onlinetutors.service.CommentService;
import com.example.onlinetutors.service.CourseService;
import com.example.onlinetutors.service.OrderService;
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
@Slf4j
@RequiredArgsConstructor
public class ParentController {

    private final CourseService courseService;
    private final UserService userService;
    private final OrderService orderService;
    private final CommentService commentService;

    @GetMapping("/home-parent")
    public String getHomePage(Model model,  HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Long id = (Long) session.getAttribute("id");
        List<Course> courses = this.orderService.handleGetCoursesByUser(id);
        log.info("Accessing home page");
        model.addAttribute("role", "PARENT");
        model.addAttribute("courses", courses);
        return "client/parent/homeParent";
    }

    @GetMapping("/parent/courses")
    public String getCoursesPage(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        List<String> subjects = List.of("TOAN",
                "LY",
                "HOA",
                "SINH",
                "ANH",
                "SU",
                "ĐIA",
                "VAN");
        if (session == null) {
            model.addAttribute("subjects", subjects);
            return "client/parent/courseParent";
        }
        String roleName = (String) session.getAttribute("role");
        model.addAttribute("subjects", subjects);
        model.addAttribute("role", roleName);
        log.info("Accessing courses page");
        return "client/parent/courseParent";
    }

    @GetMapping("/parent/courses/details")
    public String getCoursesDetailsPage(Model model, @RequestParam("id") String id, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            String roleName = (String) session.getAttribute("role");
            model.addAttribute("role", roleName);
        }
        List<Course> courses = this.courseService.handleGetCoursesBySubject(id);
        model.addAttribute("courses", courses);
        log.info("Accessing course details page");
        return "client/parent/listCourse";
    }

    @GetMapping("/parent/profile")
    public String getProfilePage(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        String email = (String) session.getAttribute("email");
        User parent = this.userService.getUserByEmail(email);
        model.addAttribute("role", "PARENT");
        model.addAttribute("parent", parent);
        log.info("Accessing profile page");
        return "client/parent/parentProfile";
    }

    @GetMapping("/parent/profile/update")
    public String updateParentProfile(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        String email = (String) session.getAttribute("email");
        User user = this.userService.getUserByEmail(email);
        model.addAttribute("parent", user);
        log.info("Accessing update profile page");
        return "client/parent/updateProfileParent";
    }

    @PostMapping("/parent/update-profile")
    public String postUpdateTutorProfile(@ModelAttribute("parent") User user,
                                         @RequestParam("imageUser") MultipartFile file) {
        Role role = new Role();
        role.setName("PARENT");
        user.setRole(role);
        this.userService.handleEditUser(user, file);
        return "redirect:/parent/profile";
    }

    @GetMapping("/parent/details")
    public String getParentDetailsPage(Model model,
                                       @RequestParam("id") Long id,
                                       HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            String roleName = (String) session.getAttribute("role");
            model.addAttribute("role", roleName);
        }
        Course course = this.courseService.handleGetCourseById(id);
        User author = this.userService.getUserByEmail(course.getAuthor());
        List<Comment> comments = this.commentService.getCommentsByCourseId(id);
        if (comments != null) {
            model.addAttribute("comments", comments);
        }

        model.addAttribute("course", course);
        model.addAttribute("author", author);
        model.addAttribute("note", "Thanh toán hóa đơn");

        log.info("Accessing parent details page");
        return "client/parent/courseDetailed";
    }

}
