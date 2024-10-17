package com.lab1.lab1.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;
import com.fasterxml.jackson.databind.SerializationFeature;

@Provider
public class ObjectMapperProvider implements ContextResolver<ObjectMapper> {

    private final ObjectMapper objectMapper;

    public ObjectMapperProvider() {
        objectMapper = new ObjectMapper();
        // Регистрация модуля для Java 8 Date/Time API
        objectMapper.registerModule(new JavaTimeModule());
        // Отключение сериализации дат как временных меток (timestamps)
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // Дополнительные настройки, если необходимо
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return objectMapper;
    }
}
