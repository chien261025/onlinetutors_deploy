package com.example.onlinetutors.repository;

import com.example.onlinetutors.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByAuthor(String author);
    List<Course> findBySubject(String subject);
}
