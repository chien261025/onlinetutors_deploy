package com.example.onlinetutors.repository;

import com.example.onlinetutors.model.Comment;
import com.example.onlinetutors.util.enumclass.StatusCommentEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByCourseIdAndStatusCommentOrderByCreatedAtDesc(Long courseId, StatusCommentEnum status);

}
