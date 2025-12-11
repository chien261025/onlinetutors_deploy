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
public class EventBookingResponseDTO
{
    private Long id;
    private String day;
    private String startTime;
    private String endTime;
    private Long tutorId;
    private Long parentId;
}
