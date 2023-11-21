package com.example.libraryproject.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "readers")
@NoArgsConstructor
public class Reader extends User {
    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "phone")
    private String phone;

    @Column(name = "place_to_live")
    private String placeToLive;

    @OneToMany()
    @JoinColumn(name = "id")
    private List<Book> books;


    @Override
    public String toString() {
        return "Reader{" + super.toString() +
                "phone='" + phone + '\'' +
                ", placeToLive='" + placeToLive + '\'' +
                ", books=" + books +
                '}';
    }
}
