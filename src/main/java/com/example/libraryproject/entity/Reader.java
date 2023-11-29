package com.example.libraryproject.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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
}
