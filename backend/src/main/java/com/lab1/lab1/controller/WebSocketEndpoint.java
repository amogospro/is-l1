package com.lab1.lab1.controller;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/updates")
public class WebSocketEndpoint {

    private static Set<Session> clients = Collections.synchronizedSet(new HashSet<>());

    @OnOpen
    public void onOpen(Session session) {
        clients.add(session);
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        for (Session client : clients) {
            if (client.isOpen()) {
                client.getBasicRemote().sendText(message);
            }
        }
    }

    @OnClose
    public void onClose(Session session) {
        clients.remove(session);
    }

    public static void sendUpdate(String updateJson) throws IOException {
        for (Session client : clients) {
            if (client.isOpen()) {
                client.getBasicRemote().sendText(updateJson);
            }
        }
    }
}
