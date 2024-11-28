package com.lab1.lab1.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lab1.lab1.model.entities.Organization;
import com.lab1.lab1.model.entities.Role;
import com.lab1.lab1.model.entities.User;
import com.lab1.lab1.model.mapper.OrganizationMapper;
import com.lab1.lab1.repository.OrganizationRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class OrganizationService {
    @Inject
    private OrganizationRepository organizationRepository;

    @Transactional
    public void createOrganization(Organization organization, User user) throws Exception {
        if (organizationRepository.findByName(organization.getName()) != null) {
            throw new Exception("Организация с таким названием уже существует");
        }

        organization.setUserOwner(user);
        organizationRepository.create(organization);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String updateJson = objectMapper.writeValueAsString(OrganizationMapper.toDTO(organization));

        // Отправляем данные асинхронно через WebSocket
        WebSocketService.sendUpdateAsync(updateJson);
    }

    public Organization getOrganizationById(Integer id) { return organizationRepository.findById(id); }

    @Transactional
    public void updateOrganization(Organization organization, User user) throws Exception {
        Organization currentOrganization = getOrganizationById(organization.getId());

        if (currentOrganization.getUserOwner().equals(user) || user.getRole() == Role.ADMIN) {
            currentOrganization.setName(organization.getName());
            currentOrganization.setOfficialAddress(organization.getOfficialAddress());
            currentOrganization.setAnnualTurnover(organization.getAnnualTurnover());
            currentOrganization.setEmployeesCount(organization.getEmployeesCount());
            currentOrganization.setRating(organization.getRating());
            currentOrganization.setType(organization.getType());
            organizationRepository.update(currentOrganization);

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            String updateJson = objectMapper.writeValueAsString(OrganizationMapper.toDTO(currentOrganization));

            // Отправляем данные асинхронно через WebSocket
            WebSocketService.sendUpdateAsync(updateJson);
        } else {
            throw new Exception("Access Denied");
        }
    }

    public void deleteOrganization(int id, User user) throws Exception {
        Organization organization = organizationRepository.findById(id);
        if (organization != null) {
            // Check if user is owner or admin
            if (organization.getUserOwner().equals(user) || user.getRole() == Role.ADMIN) {
                // Check for related objects
                // If related objects exist, throw exception
                organizationRepository.delete(organization);

                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());
                String updateJson = objectMapper.writeValueAsString(OrganizationMapper.toDTO(organization));

                // Отправляем данные асинхронно через WebSocket
                WebSocketService.sendUpdateAsync(updateJson);
            } else {
                throw new Exception("Access Denied");
            }
        }
    }

    public List<Organization> getAllOrganizations(int page, int size) {
        int offset = (page - 1) * size;
        return organizationRepository.findAll(offset, size);
    }
}
