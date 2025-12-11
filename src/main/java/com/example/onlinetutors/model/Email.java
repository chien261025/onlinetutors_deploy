package com.example.onlinetutors.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Email {

    @NotBlank(message = "Recipient email cannot be blank")
    private String to;

    private String subject;

    @NotBlank(message = "Email body cannot be blank")
    private String body;

}
