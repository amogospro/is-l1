package com.lab1.lab1.model.dto;

import com.lab1.lab1.model.entities.Address;
import com.lab1.lab1.model.entities.OrganizationType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class OrganizationDTO {
    private Integer id;
    private String name;
    private Address officialAddress;
    private Float annualTurnover;
    private int employeesCount;
    private long rating;
    private OrganizationType type;
    private UserDTO userOwner;
    private Date updatedAt;
}
