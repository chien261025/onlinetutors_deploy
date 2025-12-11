package com.example.onlinetutors.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventResponseDTO
{
    private Long id;
    private String title;
    private String daysOfWeek;
    private String startTime;
    private String endTime;
    private int weeks;
    private String startRecur;
    private String color;
    private Long courseId;
    private Long tutorId;
}
