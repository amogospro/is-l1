package com.lab1.lab1.MinIOTransaction;

public interface TransactionalResource {
    void prepare() throws Exception; // Проверка готовности ресурса
    void commit() throws Exception;  // Фиксация транзакции
    void rollback();                 // Откат изменений
}

