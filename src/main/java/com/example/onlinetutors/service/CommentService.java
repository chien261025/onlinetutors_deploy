package com.example.onlinetutors.service;

import com.example.onlinetutors.model.Comment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentService {

    List<Comment> getCommentsByCourseId(Long courseId);
    Comment addComment(Comment comment);
    List<Comment> getAllComments();
    Comment getCommentById(Long commentId);
    void handleUpdateCommentStatus(String status, Long commentId);
    void deleteCommentsByCourseId(Long courseId);
}
