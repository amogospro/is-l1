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
                WebSocketEndpoint.sendUpdate(message); // Отправка через WebSocket
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
