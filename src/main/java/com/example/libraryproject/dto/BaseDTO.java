package com.example.libraryproject.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BaseDTO {
    private Long id;
    private LocalDateTime createdAt;
}
