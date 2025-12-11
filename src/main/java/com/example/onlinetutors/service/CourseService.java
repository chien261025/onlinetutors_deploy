package com.example.onlinetutors.service;

import com.example.onlinetutors.model.Course;
import com.example.onlinetutors.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CourseService {
    void handleCreateCourse(Course course);
    List<Course> handleGetCoursesByEmail(String email);
    List<Course> handleGetCoursesBySubject(String subject);
    List<Course> handleGetAllCourses();
    Course handleGetCourseById(Long id);
    void handleDeleteCourseById(Long id);
    void handleUpdateCourse(Course course);

}
