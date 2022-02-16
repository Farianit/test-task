package com.trackensure.testtask.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/chat")
public class ChatController extends HttpServlet {

    private final String PAGE = "/chat.jsp";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // If username is not provided in request, return to login page.
        String name = req.getParameter("name");
        if (name == null) {
            resp.sendRedirect(req.getContextPath());
            return;
        }
        req.getRequestDispatcher(PAGE).forward(req, resp);
    }
}
