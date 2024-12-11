package com.lab1.lab1.MinIOTransaction;

import java.util.ArrayList;
import java.util.List;

public class TwoPhaseTransactionCoordinator {
    private final List<TransactionalResource> resources = new ArrayList<>();

    public void addResource(TransactionalResource resource) {
        resources.add(resource);
    }

    public void execute() throws Exception {
        try {
            // Шаг 1: Подготовка всех ресурсов
            for (TransactionalResource resource : resources) {
                resource.prepare();
            }

            // Шаг 2: Фиксация транзакции
            for (TransactionalResource resource : resources) {
                resource.commit();
            }
        } catch (Exception e) {
            // Шаг 3: Откат в случае ошибки
            for (TransactionalResource resource : resources) {
                resource.rollback();
            }
            throw new Exception("Transaction failed and was rolled back.", e);
        }
    }
}

