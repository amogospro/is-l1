package com.lab1.lab1.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lab1.lab1.MinIOTransaction.DatabaseTransactionalResource;
import com.lab1.lab1.MinIOTransaction.MinIOTransactionalResource;
import com.lab1.lab1.MinIOTransaction.TwoPhaseTransactionCoordinator;
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
import io.minio.GetObjectArgs;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.core.Response;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class ImportService {

    @Inject
    private MinIOService minioService;
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

    private boolean simulateError = false;

    @Transactional
    public void importObjects(InputStream fileInputStream, User user, String fileName) throws Exception {
        if (simulateError) {
            throw new RuntimeException("Беды с башкой");
        }

        String bucketName = "imported-files";

        if (fileName.length() > 255) {
            fileName = fileName.substring(0, 255);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        fileInputStream.transferTo(baos);

        InputStream minioInputStream = new ByteArrayInputStream(baos.toByteArray());
        InputStream jsonInputStream = new ByteArrayInputStream(baos.toByteArray());

        TwoPhaseTransactionCoordinator coordinator = new TwoPhaseTransactionCoordinator();
        coordinator.addResource(new MinIOTransactionalResource(minioService, bucketName, fileName, minioInputStream));

        try {
            // Используем Jackson для парсинга JSON
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            // Чтение JSON с объектами Product
            ProductsWrapper wrapper = objectMapper.readValue(jsonInputStream, ProductsWrapper.class);

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
                    coordinator.addResource(new DatabaseTransactionalResource(productService, product, user));

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

                    successCount++;
                } catch (ValidationException e) {
                    throw new Exception("Error at line " + lineNumber + ": " + e.getMessage(), e);
                }
            }

            // Подготовка и фиксация файла в MinIO
            coordinator.execute();

            logImportHistory(user.getUsername(), "SUCCESS", successCount, fileName);

        } catch (Exception e) {
            throw new Exception("MinIOException", e);
        }

    }

    public void logImportHistory(String userName, String status, int count, String fileName) {
        // Логирование истории импорта
        ImportHistory history = new ImportHistory();
        history.setFileName(fileName);
        history.setUserName(userName);
        history.setTimestamp(LocalDateTime.now());
        history.setStatus(status);
        history.setImportedObjectsCount(count);
        importHistoryRepository.create(history);
    }

    public List<ImportHistory> getImportHistory(User user) {
        return importHistoryRepository.findByUser(user);
    }

    public Response downloadFileFromMinIO(String bucketName, String fileName) {
        try {
            InputStream fileStream = minioService.downloadFile(bucketName, fileName);

            // Возвращаем файл как ответ
            return Response.ok(fileStream)
                    .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Ошибка при скачивании файла").build();
        }
    }
}

