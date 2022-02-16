package com.trackensure.testtask.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Logger;

@WebServlet("/chat")
public class ChatController extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(MessagesController.class.getName());

    private final String PAGE = "/chat.jsp";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // If username is not provided in request, return to login page.
        LOGGER.info("Received GET request to /chat");
        String name = req.getParameter("name");
        if (name == null) {
            LOGGER.info("Username is not provided while trying to access chat page, redirecting");
            resp.sendRedirect(req.getContextPath());
            return;
        }
        LOGGER.info("Forwarding to " + PAGE);
        req.getRequestDispatcher(PAGE).forward(req, resp);
    }
}
