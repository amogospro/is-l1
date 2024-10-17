package com.lab1.lab1.model.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Embeddable
@Data
@NoArgsConstructor
public class Location {
    private int x;
    private float y;
    private double z;
}

