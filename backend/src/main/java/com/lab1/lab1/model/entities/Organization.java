package com.lab1.lab1.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@Table(name = "organization")
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @NotNull
    @NotBlank
    private String name;

    @Embedded
    private Address officialAddress;

    @Positive(message = "Annual turnover must be greater than 0")
    private Float annualTurnover;

    @Positive(message = "Employees count must be greater than 0")
    private int employeesCount;

    @Positive(message = "Rating must be greater than 0")
    private long rating;

    @Enumerated(EnumType.STRING)
    private OrganizationType type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id", nullable = false)
    private User userOwner;

    // Additional fields like creation and update timestamps
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt = new Date();
}
