package com.example.onlinetutors.repository;

import com.example.onlinetutors.model.Event;
import com.example.onlinetutors.model.UserEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserEventRepository extends JpaRepository<UserEvent, Long> {

    @Query("SELECT e FROM Event e JOIN UserEvent ue ON e.id = ue.event.id WHERE ue.user.id = :userId")
    List<Event> findEventsByUserEventRegistration(@Param("userId") Long userId);
}
