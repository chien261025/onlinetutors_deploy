package com.example.onlinetutors.controller;

import com.example.onlinetutors.model.Comment;
import com.example.onlinetutors.model.Course;
import com.example.onlinetutors.model.User;
import com.example.onlinetutors.service.CommentService;
import com.example.onlinetutors.service.CourseService;
import com.example.onlinetutors.service.UserService;
import com.example.onlinetutors.util.enumclass.StatusCommentEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class CommentController {

    private final UserService userService;
    private final CourseService courseService;
    private final CommentService commentService;

    @PostMapping("/course/{id}/comment")
    public String addComment(@PathVariable("id") Long courseId,
                             @RequestParam("content") String content,
                             HttpServletRequest request
    ) {
        HttpSession session = request.getSession(false);
        String email = (String) session.getAttribute("email");
        User user = this.userService.getUserByEmail(email);
        Course course = this.courseService.handleGetCourseById(courseId);
        Comment comment = Comment.builder()
                .statusComment(StatusCommentEnum.VISIBLE)
                .content(content)
                .course(course)
                .user(user)
                .build();
        this.commentService.addComment(comment);
        log.info("Posting a comment");
        return "redirect:/parent/details?id=" + courseId;
    }

    @GetMapping("/admin/comments")
    public String getAllComments(Model model) {
        List<Comment> comments = this.commentService.getAllComments();
        model.addAttribute("comments", comments);
        return "admin/course/adminCommentPage";
    }

    @PostMapping("admin/comments/status")
    public String changeCommentStatus(@RequestParam("id") Long commentId,
                                      @RequestParam("status") String status) {
        this.commentService.handleUpdateCommentStatus(status, commentId);
        return "redirect:/admin/comments";
    }

    @PostMapping("/admin/comments/delete")
    public String deleteComment(@RequestParam("id") Long commentId) {
        this.commentService.deleteCommentsByCourseId(commentId);
        return "redirect:/admin/comments";
    }

}
