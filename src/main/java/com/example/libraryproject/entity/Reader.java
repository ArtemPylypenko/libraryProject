package com.example.libraryproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "readers")
@NoArgsConstructor
@AllArgsConstructor
public class Reader extends User {
    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "phone")
    private String phone;

    @Column(name = "place_to_live")
    private String placeToLive;

    @ManyToMany(fetch = FetchType.LAZY)
    @Cascade({org.hibernate.annotations.CascadeType.ALL})
    @JoinTable(
            name = "book_history",
            joinColumns = {@JoinColumn(name = "reader_id")},
            inverseJoinColumns = {@JoinColumn(name = "book_id")}
    )
    private Set<Book> books = new HashSet<>();
}
