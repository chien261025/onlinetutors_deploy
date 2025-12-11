package com.example.onlinetutors.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "event_bookings")
public class EventBooking  extends  AbstractEntity {


    @Column(name = "days")
    private LocalDate day;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "weeks")
    private String color;

    @ManyToOne
    @JoinColumn(name = "tutor_id")
    private User tutor;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private User parent;

}
