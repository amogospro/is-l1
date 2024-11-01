package com.lab1.lab1.model.entities;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@Table(name = "person")
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id", nullable = false)
    private User userOwner;

    // Additional fields like creation and update timestamps
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt = new Date();
}

