package com.example.LMS.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorDTO {

    private UUID id;

    @NotNull(message = "Author name cannot be null")
    @NotBlank(message = "Author name cannot be blank")
    @Size(min = 1, max = 100, message = "Author name must be between 1 and 100 characters")
    private String name;

    @Size(max = 1000, message = "Biography cannot exceed 1000 characters")
    private String biography;

    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;

    @Size(max = 50, message = "Nationality cannot exceed 50 characters")
    private String nationality;

    private int totalBooks;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

