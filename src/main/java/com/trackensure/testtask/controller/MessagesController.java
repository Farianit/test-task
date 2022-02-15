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

@WebServlet("/messages")
public class MessagesController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String name = req.getParameter("name");
        String text = req.getParameter("text");

        if (name != null && text != null) {
            text = text.trim();
            sendMessage(name, text);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        // Get last 50 messages from database
        MessageDAO messageDAO = new MessageDAO();
        Set<Message> messages = messageDAO.getAll(50, "sent_time DESC");
        try {
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
        User author = userDAO.createOrGetUserByName(name);

        MessageDAO messageDAO = new MessageDAO();
        Message newMessage = new Message(author, text);
        newMessage = messageDAO.insert(newMessage);

        try {
            String messageJSON = new ObjectMapper().writeValueAsString(newMessage);
            MessageWebSocket.sendMessage(messageJSON);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
