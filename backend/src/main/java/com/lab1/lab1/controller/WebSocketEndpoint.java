package com.lab1.lab1.controller;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/updates")
public class WebSocketEndpoint {

    private static Set<Session> sessions = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        // Optionally send a welcome message
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // Handle incoming messages if needed
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
    }

    public static void broadcast(String message) {
        sessions.forEach(session -> {
            session.getAsyncRemote().sendText(message);
        });
    }
}
