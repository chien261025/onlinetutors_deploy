package com.example.onlinetutors.controller;

import com.example.onlinetutors.model.Course;
import com.example.onlinetutors.model.Email;
import com.example.onlinetutors.model.Signup;
import com.example.onlinetutors.model.User;
import com.example.onlinetutors.service.CourseService;
import com.example.onlinetutors.service.impl.EmailService;
import com.example.onlinetutors.service.EventService;
import com.example.onlinetutors.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class DashboardController {

    private final EmailService emailService;
    private final UserService userService;
    private final CourseService courseService;
    private final EventService eventService;

    @GetMapping("/admin")
    public String getDashboardPage() {
        return "admin/index";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        return "admin/login";
    }


    @GetMapping({"/email", "/verification-email"})
    public String getEmailPage(Model model) {
        model.addAttribute("email", new Email());
        return "admin/email";
    }

    @GetMapping("/signup")
    public String getSignupPage(Model model) {
        model.addAttribute("newSignUp", new Signup());
        return "admin/signup";
    }

    @PostMapping("/signup")
    public String handleSignup(Model model, @Valid @ModelAttribute("newSignUp") Signup signup) {
        try {
            userService.signupUser(signup);
            model.addAttribute("message", "Vui lòng kiểm tra email để xác thực tài khoản!");
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "admin/signup";
        }
        return "admin/signup";
    }

    @GetMapping("/confirm-email")
    public String handleConfirmEmail(@RequestParam("secretCode") String secretCode) {
        userService.verifyUser(secretCode);
        return "redirect:/login";
    }

    @GetMapping("/")
    public String getHomePage(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        List<User> tutors = this.userService.getUsersByRoleId(3L);
        List<Course> courses = this.courseService.handleGetAllCourses();
        List<Course> subList = courses.subList(0, Math.min(6, courses.size()));
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
            model.addAttribute("tutors", tutors);
            model.addAttribute("courses", subList);
            return "client/home";
        }
        String roleName = (String) session.getAttribute("role");
        model.addAttribute("role", roleName);
        model.addAttribute("subjects", subjects);
        model.addAttribute("tutors", tutors);
        model.addAttribute("courses", subList);
        return "client/home";
    }

    @GetMapping("/teacher")
    public String getTeacherPage(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        List<User> tutors = this.userService.getUsersByRoleId(3L);
        if(session == null) {
            model.addAttribute("tutors", tutors);
            return "client/homeTeacher";
        }
        String roleName = (String) session.getAttribute("role");
        model.addAttribute("role", roleName);
        model.addAttribute("tutors", tutors);
        return "client/homeTeacher";
    }

    @GetMapping("/forgotPassword")
    public String getForgotPasswordPage() {
        return "admin/forgotPassword";
    }

    @PostMapping("/find-email")
    public String handleFindEmail(Model model, @RequestParam("email") String email) {
        try {
            userService.sendResetLink(email);
            model.addAttribute("message", "Vui lòng kiểm tra email để đặt lại mật khẩu!");
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "admin/forgotPassword";
        }
        return "admin/forgotPassword";
    }

    @GetMapping("/reset-password")
    public String getResetPasswordPage(@RequestParam("resetToken") String resetToken, Model model) {
        model.addAttribute("token", resetToken);
        return "resetPassword";
    }

    @PostMapping("confirm-password")
    public String handleConfirmPassword(@RequestParam("token") String token,
                                        @RequestParam("password") String password,
                                        @RequestParam("confirmPassword") String confirmPassword,
                                        Model model) {
        if(!password.equals(confirmPassword)){
            model.addAttribute("error", "Mật khẩu xác nhận không khớp!");
            model.addAttribute("token", token);
            return "admin/resetPassword";
        }
        userService.resetPassword(token, password);
        return "redirect:/login";
    }
}
