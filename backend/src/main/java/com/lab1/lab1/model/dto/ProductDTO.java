package com.lab1.lab1.model.dto;

import com.lab1.lab1.model.entities.Coordinates;
import com.lab1.lab1.model.entities.UnitOfMeasure;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
public class ProductDTO {
    private int id;
    private String name;
    private Coordinates coordinates;
    private LocalDate creationDate;
    private UnitOfMeasure unitOfMeasure;
    private OrganizationDTO manufacturer;
    private int price;
    private float manufactureCost;
    private Integer rating;
    private PersonDTO owner;
    private UserDTO userOwner;
    private Date updatedAt;

}