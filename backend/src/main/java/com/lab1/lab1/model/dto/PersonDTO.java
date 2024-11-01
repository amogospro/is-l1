package com.lab1.lab1.model.dto;

import com.lab1.lab1.model.entities.Color;
import com.lab1.lab1.model.entities.Country;
import com.lab1.lab1.model.entities.Location;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
public class PersonDTO {
    private int id;
    private String name;
    private Color eyeColor;
    private Color hairColor;
    private Location location;
    private LocalDateTime birthday;
    private Country nationality;
    private UserDTO userOwner;
    private Date updatedAt;
}
