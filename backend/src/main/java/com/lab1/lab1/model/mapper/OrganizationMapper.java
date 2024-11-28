package com.lab1.lab1.model.mapper;

import com.lab1.lab1.model.dto.OrganizationDTO;
import com.lab1.lab1.model.entities.Organization;

import java.util.Date;

public class OrganizationMapper {
    public static OrganizationDTO toDTO(Organization organization) {
        if (organization == null) {
            return null;
        }
        OrganizationDTO organizationDTO = new OrganizationDTO();
        organizationDTO.setId(organization.getId());
        organizationDTO.setName(organization.getName());
        organizationDTO.setOfficialAddress(organization.getOfficialAddress());
        organizationDTO.setAnnualTurnover(organization.getAnnualTurnover());
        organizationDTO.setEmployeesCount(organization.getEmployeesCount());
        organizationDTO.setRating(organization.getRating());
        organizationDTO.setType(organization.getType());
        organizationDTO.setUserOwner(UserMapper.toDTO(organization.getUserOwner()));
        organizationDTO.setUpdatedAt(organization.getUpdatedAt());
        return organizationDTO;
    }

    public static Organization toEntity(OrganizationDTO organizationDTO) {
        if (organizationDTO == null) {
            return null;
        }
        Organization organization = new Organization();
        organization.setId(organizationDTO.getId());
        organization.setName(organizationDTO.getName());
        organization.setOfficialAddress(organizationDTO.getOfficialAddress());
        organization.setAnnualTurnover(organizationDTO.getAnnualTurnover());
        organization.setEmployeesCount(organizationDTO.getEmployeesCount());
        organization.setRating(organizationDTO.getRating());
        organization.setType(organizationDTO.getType());
        organization.setUserOwner(UserMapper.toEntity(organizationDTO.getUserOwner()));
        organization.setUpdatedAt(organizationDTO.getUpdatedAt());

        if (organization.getUpdatedAt() == null) {
            organization.setUpdatedAt(new Date());
        }

        return organization;
    }
}
