package com.trackensure.testtask.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trackensure.testtask.dao.MessageDAO;
import com.trackensure.testtask.dao.UserDAO;
import com.trackensure.testtask.model.Message;
import com.trackensure.testtask.model.User;
import com.trackensure.testtask.websocket.MessageWebSocket;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.logging.Logger;

@WebServlet("/messages")
public class MessagesController extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(MessagesController.class.getName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        LOGGER.info("Received POST request to /messages, getting parameters.");
        String name = req.getParameter("name");
        String text = req.getParameter("text");

        if (name != null && text != null) {
            text = text.trim();
            LOGGER.info(String.format("Saving new message: name='%s' text='%s'", name, text));
            sendMessage(name, text);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } else {
            LOGGER.info(String.format("Wrong request parameters: name='%s' text='%s'", name, text));
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        LOGGER.info("Received GET request to /messages, getting messages from database");
        // Get last 50 messages from database
        MessageDAO messageDAO = new MessageDAO();
        Set<Message> messages = messageDAO.getAll(50, "sent_time DESC");
        try {
            LOGGER.info("Converting received messages to JSON and sending in response");
            String messagesJSON = new ObjectMapper().writeValueAsString(messages);
            PrintWriter out = resp.getWriter();
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            out.print(messagesJSON);
            out.flush();
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (IOException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Stores new message to database and sends it to all users via WebSocket
     * @param name Message author
     * @param text Message text
     */
    public void sendMessage(String name, String text) {
        UserDAO userDAO = new UserDAO();
        LOGGER.info("Getting message author id from database");
        User author = userDAO.createOrGetUserByName(name);

        MessageDAO messageDAO = new MessageDAO();
        Message newMessage = new Message(author, text);
        LOGGER.info("Inserting new message");
        newMessage = messageDAO.insert(newMessage);
        LOGGER.info("Message inserted successfully");
        try {
            LOGGER.info("Sending new message via WebSocket");
            String messageJSON = new ObjectMapper().writeValueAsString(newMessage);
            MessageWebSocket.sendMessage(messageJSON);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
