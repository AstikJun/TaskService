package com.example.mega.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskCreateDto {
    @NonNull
    private String title;

    @NonNull
    private String description;

    @NonNull
    @Email
    private String email;

    private boolean completed;
}
