package com.lab1.lab1.model.entities;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Table(name = "person")
@Entity
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // Auto-generated, must be > 0

    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name cannot be empty")
    private String name; // Not null, not empty

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Color cannot be null")
    private Color eyeColor; // Not null

    @Enumerated(EnumType.STRING)
    private Color hairColor; // Not null

    @Embedded
    private Location location; // Not null

    @NotNull
    private LocalDateTime birthday;

    @NotNull(message = "Nationality cannot be null")
    @Enumerated(EnumType.STRING)
    private Country nationality;
}

