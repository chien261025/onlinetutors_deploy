package com.example.onlinetutors.service;

import com.example.onlinetutors.model.Course;
import com.example.onlinetutors.model.Order;
import com.example.onlinetutors.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
    Order handleCreateOrder(Course course, User user);

    List<Course> handleGetCoursesByUser(Long userId);
}
