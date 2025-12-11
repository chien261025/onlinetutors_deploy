package com.example.onlinetutors.repository;

import com.example.onlinetutors.model.Course;
import com.example.onlinetutors.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT c FROM Order o JOIN o.course c WHERE o.user.id = :userId")
    List<Course> findCoursesByUser(@Param("userId") Long userId);



}
