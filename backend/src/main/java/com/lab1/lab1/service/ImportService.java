package com.lab1.lab1.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lab1.lab1.model.dto.OrganizationDTO;
import com.lab1.lab1.model.dto.PersonDTO;
import com.lab1.lab1.model.dto.ProductDTO;
import com.lab1.lab1.model.dto.ProductsWrapper;
import com.lab1.lab1.model.entities.*;
import com.lab1.lab1.model.mapper.OrganizationMapper;
import com.lab1.lab1.model.mapper.PersonMapper;
import com.lab1.lab1.model.mapper.ProductMapper;
import com.lab1.lab1.model.mapper.UserMapper;
import com.lab1.lab1.repository.ImportHistoryRepository;
import com.lab1.lab1.repository.OrganizationRepository;
import com.lab1.lab1.repository.PersonRepository;
import com.lab1.lab1.repository.ProductRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class ImportService {

    @Inject
    private OrganizationService organizationService;
    @Inject
    private PersonService personService;
    @Inject
    private ProductService productService;

    @Inject
    private PersonRepository personRepository;

    @Inject
    private OrganizationRepository organizationRepository;

    @Inject
    private ImportHistoryRepository importHistoryRepository;

    @Transactional
    public void importObjects(InputStream fileInputStream, User user) throws Exception {
        // Используем Jackson для парсинга JSON
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Чтение JSON с объектами Product
        ProductsWrapper wrapper = objectMapper.readValue(fileInputStream, ProductsWrapper.class);

        int lineNumber = 0;
        int successCount = 0;

        // Обрабатываем каждый продукт
        for (ProductDTO productDTO : wrapper.getProducts()) {
            lineNumber++;
            try {
                productDTO.setUserOwner(UserMapper.toDTO(user));
                if(productDTO.getOwner() != null) {
                    productDTO.getOwner().setUserOwner(UserMapper.toDTO(user));
                }

                if (productDTO.getManufacturer() != null) {
                    productDTO.getManufacturer().setUserOwner(UserMapper.toDTO(user));
                }

                Product product = ProductMapper.toEntity(productDTO);

                PersonDTO personDTO = productDTO.getOwner();
                OrganizationDTO organizationDTO = productDTO.getManufacturer();

                if (personDTO.getId() != 0 || personDTO != null) {
                    Person existingOwner = personRepository.findById(personDTO.getId());

                    if (existingOwner == null) {
                        personDTO.setUserOwner(UserMapper.toDTO(user));
                        Person person = PersonMapper.toEntity(personDTO);
                        personService.createPerson(person, user);
                        product.setOwner(person);
                    } else {
                        product.setOwner(existingOwner);
                    }
                }

                if (organizationDTO.getId() != null || organizationDTO != null) {
                    Organization existingOrganization = organizationRepository.findById(organizationDTO.getId());

                    if (existingOrganization == null) {
                        organizationDTO.setUserOwner(UserMapper.toDTO(user));
                        Organization organization = OrganizationMapper.toEntity(organizationDTO);
                        organizationService.createOrganization(organization, user);
                        product.setManufacturer(organization);
                    } else {
                        product.setManufacturer(existingOrganization);
                    }

                }

//                if (personDTO != null) {
//                    personDTO.setUserOwner(UserMapper.toDTO(user));
//                    Person person = PersonMapper.toEntity(personDTO);
//                    personService.createPerson(person, user);
//                    product.setOwner(person);
//                }

//                if (organizationDTO != null) {
//                    organizationDTO.setUserOwner(UserMapper.toDTO(user));
//                    Organization organization = OrganizationMapper.toEntity(organizationDTO);
//                    organizationService.createOrganization(organization, user);
//                    product.setManufacturer(organization);
//                }

                productService.createProduct(product, user);

                successCount++;
            } catch (ValidationException e) {
                throw new Exception("Error at line " + lineNumber + ": " + e.getMessage(), e);
            }
        }

        logImportHistory(user.getUsername(), "SUCCESS", successCount);
    }

    public void logImportHistory(String userName, String status, int count) {
        // Логирование истории импорта
        ImportHistory history = new ImportHistory();
        history.setUserName(userName);
        history.setTimestamp(LocalDateTime.now());
        history.setStatus(status);
        history.setImportedObjectsCount(count);
        importHistoryRepository.create(history);
    }

    public List<ImportHistory> getImportHistory(User user) {
        return importHistoryRepository.findByUser(user);
    }
}

