package com.example.libraryproject.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "book_history")
@NoArgsConstructor
public class BookHistory extends BaseEntity{
    @OneToOne
    private Reader user;

    @OneToOne
    private Book book;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime takeTime;

    @Column()
    private LocalDateTime returnTime;
}
