package com.example.onlinetutors.service.impl;

import com.example.onlinetutors.model.Comment;
import com.example.onlinetutors.repository.CommentRepository;
import com.example.onlinetutors.service.CommentService;
import com.example.onlinetutors.util.enumclass.StatusCommentEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;


    @Override
    public List<Comment> getCommentsByCourseId(Long courseId) {
        return this.commentRepository.findByCourseIdAndStatusCommentOrderByCreatedAtDesc(courseId, StatusCommentEnum.VISIBLE);
    }

    @Override
    public Comment addComment(Comment comment) {
        return this.commentRepository.save(comment);
    }

    @Override
    public List<Comment> getAllComments() {
        return this.commentRepository.findAll();
    }

    @Override
    public Comment getCommentById(Long commentId) {
        return this.commentRepository.findById(commentId).orElseThrow(() -> {
            log.error("Comment with id {} not found", commentId);
            return new RuntimeException("Comment not found");
        });
    }

    @Override
    public void handleUpdateCommentStatus(String status, Long commentId) {
        Comment comment = getCommentById(commentId);
        if (status.equals("HIDDEN")) {
            comment.setStatusComment(StatusCommentEnum.HIDDEN);
        } else if (status.equals("VISIBLE")) {
            comment.setStatusComment(StatusCommentEnum.VISIBLE);
        }
        this.commentRepository.save(comment);
    }

    @Override
    public void deleteCommentsByCourseId(Long courseId) {
        Comment comment = this.getCommentById(courseId);
        comment.setStatusComment(
                StatusCommentEnum.DELETED
        );
        this.commentRepository.save(comment);
    }

}
