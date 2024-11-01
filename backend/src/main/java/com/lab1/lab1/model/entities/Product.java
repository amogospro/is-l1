package com.lab1.lab1.model.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "product")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id; // Auto-generated, must be > 0

    @NotNull
    @NotBlank
    private String name; // Not null, not empty

    @Embedded
    @NotNull
    private Coordinates coordinates; // Not null

    @NotNull
    private LocalDate creationDate = LocalDate.now(); // Auto-generated, not null

    @Enumerated(EnumType.STRING)
    private UnitOfMeasure unitOfMeasure;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "organization_id")
    private Organization manufacturer;

    @Positive(message = "Price must be greater than 0")
    private int price;

    private float manufactureCost;

    @Positive(message = "Rating must be greater than 0")
    private Integer rating;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "person_id")
    @NotNull
    private Person owner;

    // Owner of the product
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id", nullable = false)
    private User userOwner;

    // Additional fields like creation and update timestamps
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt = new Date();
}
