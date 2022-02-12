package com.trackensure.testtask.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trackensure.testtask.dao.MessageDAO;
import com.trackensure.testtask.dao.UserDAO;
import com.trackensure.testtask.model.Message;
import com.trackensure.testtask.model.User;
import com.trackensure.testtask.websocket.MessageWebSocket;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Set;

@WebServlet("/chat")
public class ChatController extends HttpServlet {

    private final String PAGE = "/WEB-INF/chat.jsp";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        if (name == null) {
            resp.sendRedirect(req.getContextPath());
            return;
        }
        name = name.trim();

        String text = req.getParameter("text");
        if (text != null) {
            text = text.trim();
            sendMessage(name, text);
        }

        MessageDAO messageDAO = new MessageDAO();
        Set<Message> messages = messageDAO.getAll(50, "sent_time DESC");
        req.setAttribute("messages", messages);
        req.getRequestDispatcher(PAGE).forward(req, resp);
    }

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
