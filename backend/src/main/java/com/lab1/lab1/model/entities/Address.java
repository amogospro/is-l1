package com.lab1.lab1.model.entities;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
public class Address {
    private String zipCode;

    @Embedded
    private Location town;
}
