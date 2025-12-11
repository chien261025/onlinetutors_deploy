package com.example.onlinetutors.repository;

import com.example.onlinetutors.model.Course;
import com.example.onlinetutors.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByCourse_Id(Long courseId);
    Event findEventByCourse(Course course);

}
