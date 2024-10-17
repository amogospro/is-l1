package com.lab1.lab1.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name = "organization")
@Entity
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
}
