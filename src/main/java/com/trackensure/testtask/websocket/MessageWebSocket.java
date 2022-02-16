package com.trackensure.testtask.websocket;

import com.trackensure.testtask.controller.MessagesController;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

@ServerEndpoint("/websocket")
public class MessageWebSocket {
    private static final Logger LOGGER = Logger.getLogger(MessagesController.class.getName());
    private static final Set<Session> SESSIONS = new HashSet<>();

    @OnOpen
    public void open(Session session) {
        LOGGER.info("Opening new websocket session");
        SESSIONS.add(session);
    }

    /**
     * Send message to all active WebSocket clients.
     * @param message Message text
     */
    public static void sendMessage(String message) {
        LOGGER.info(String.format("Sending message '%s' via WebSocket", message));
        for (Session session : SESSIONS) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClose
    public void close(Session session) {
        LOGGER.info("Closing WebSocket session");
        try {
            session.close();
            SESSIONS.remove(session);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnError
    public void onError(Session session, Throwable e) {
        close(session);
        SESSIONS.remove(session);
    }
}
