package com.lab1.lab1.model.entities;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;


@Embeddable
@Data
@NoArgsConstructor
public class Coordinates {

    @NotNull
    @Max(864)
    private int x; // Max value: 864

    @NotNull
    private Long y; // Not null
}

