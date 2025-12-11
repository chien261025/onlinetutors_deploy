package com.example.onlinetutors.service.impl;

import com.example.onlinetutors.model.Course;
import com.example.onlinetutors.model.Order;
import com.example.onlinetutors.model.User;
import com.example.onlinetutors.repository.OrderRepository;
import com.example.onlinetutors.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public Order handleCreateOrder(Course course, User user) {
        Order order = new Order();
        order.setCourse(course);
        order.setUser(user);
        log.info("Creating order for user: {} and course: {}", user.getEmail(), course);
        return this.orderRepository.save(order);
    }

    @Override
    public List<Course> handleGetCoursesByUser(Long userId) {
        return this.orderRepository.findCoursesByUser(userId);
    }
}
