package com.lab1.lab1.service;

import com.lab1.lab1.controller.WebSocketEndpoint;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebSocketService {

    private static final ExecutorService executor = Executors.newCachedThreadPool();

    public static void sendUpdateAsync(String message) {
        executor.submit(() -> {
            try {
                System.out.println("[" + LocalDateTime.now() + "] Starting WebSocket update...");
                // Искусственная задержка для демонстрации асинхронности
                Thread.sleep(3000); // Задержка 3 секунды
                WebSocketEndpoint.sendUpdate(message); // Отправка через WebSocket
                System.out.println("[" + LocalDateTime.now() + "] WebSocket update completed.");
            } catch (Exception e) {
                e.printStackTrace(); // Логируем ошибки, чтобы не потерять их
            }
        });
    }
}
