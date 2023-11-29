package com.example.libraryproject.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity

@Data
@Table(name = "books")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Book extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "authors")
    private String authors;

    @Column(name = "publication")
    private int publication;

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "given_by")
    private String givenBy;

    @Column(name = "available")
    private boolean available;

    @Column(name = "rating")
    private Double rating;
}
