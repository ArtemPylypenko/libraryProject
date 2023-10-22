package com.example.libraryproject.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
public abstract class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime createdTime;
}
