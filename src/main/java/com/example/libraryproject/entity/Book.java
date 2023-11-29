package com.example.libraryproject.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity

@Data
@Table(name = "books")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
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

    @ManyToMany(mappedBy = "books", cascade = {CascadeType.DETACH})
    private Set<Reader> readers = new HashSet<>();

    @Override
    public int hashCode() {
        return Objects.hash(authors, name, givenBy);
    }
}
