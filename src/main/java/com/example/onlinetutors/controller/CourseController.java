package com.example.onlinetutors.controller;

import com.example.onlinetutors.model.Course;
import com.example.onlinetutors.model.Event;
import com.example.onlinetutors.service.CourseService;
import com.example.onlinetutors.service.EventService;
import com.example.onlinetutors.service.impl.FileService;
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
public class CourseController {

    private final CourseService courseService;
    private final FileService uploadFileService;
    private final EventService eventService;

    @GetMapping("/tutor/profile/create-course")
    public String getCreateCourse(Model model) {
        Course course = new Course();
        model.addAttribute("course", course);
        model.addAttribute("role", "TUTOR");
        return "client/tutor/createCourse";
    }

    @PostMapping("tutor/createCourse")
    public String postCreateCourse(
            @ModelAttribute("course") Course course,
            HttpServletRequest request,
            @RequestParam("imageCourse") MultipartFile  file
    ) {
        HttpSession session = request.getSession(false);
        String email = (String) session.getAttribute("email");
        log.info("Course Info: {}", course);
        course.setAuthor(email);
        String uploadFile = this.uploadFileService.handleSaveUploadFile(file, "uploads/client/images");
        course.setImageUrl(uploadFile);
        this.courseService.handleCreateCourse(course);
        return "redirect:/tutor/profile";
    }

    @GetMapping("admin/courses")
    public String getAdminCoursesPage(Model model) {
        List<Course> courses = this.courseService.handleGetAllCourses();
        model.addAttribute("courses", courses);
        return "admin/course/adminCoursePage";
    }

    @PostMapping("admin/course/delete")
    public String postDeleteCourse(@RequestParam("id") Long id) {
        this.courseService.handleDeleteCourseById(id);
        return "redirect:/admin/courses";
    }

    @GetMapping("admin/course/edit")
    public String getEditCoursePage(Model model, @RequestParam("id") Long id) {
        Course course = this.courseService.handleGetCourseById(id);
        if (course == null) {
            log.info("Course not found with id: {}", id);
            return "redirect:/admin/courses";
        }
        Event eventByCourse = this.eventService.getEventByCourseId(course);
        if(eventByCourse == null) {
            eventByCourse = new Event();
        }
        model.addAttribute("event", eventByCourse);
        model.addAttribute("course", course);
        return "admin/course/adminCourseEdit";
    }



    @PostMapping("course/edit")
    public String postEditCourse(
            @ModelAttribute("course") Course course,
            @RequestParam("imageCourse") MultipartFile  file
    ) {
        Course existingCourse = this.courseService.handleGetCourseById(course.getId());

        if(!file.isEmpty()) {
            String uploadFile = this.uploadFileService.handleSaveUploadFile(file, "uploads/client/images");
            existingCourse.setImageUrl(uploadFile);
        }
        this.courseService.handleUpdateCourse(existingCourse);
        return "redirect:/admin/courses";
    }

    @GetMapping("/course/tutor/list")
    public String getCoursesByTutorEmail(
            Model model,
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession(false);
        if(session == null) {
            return "redirect:/login";
        }
        String role = (String) session.getAttribute("role");
        model.addAttribute("role", role);
        String email = (String) session.getAttribute("email");
        List<Course> courses = this.courseService.handleGetCoursesByEmail(email);
        model.addAttribute("courses", courses);
        return "client/tutor/coursetutor";
    }

    @GetMapping("/tutor/courseUpdate")
    public String getTutorCourseUpdatePage(
            Model model,
            @RequestParam("id") Long id
    ) {
        Course course = this.courseService.handleGetCourseById(id);
        model.addAttribute("course", course);
        model.addAttribute("role", "TUTOR");
        return "client/tutor/courseUpdate";
    }

}
