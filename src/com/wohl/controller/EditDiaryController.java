package com.wohl.controller;

import com.wohl.misc.DefaultResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;

import java.time.LocalDateTime;

@WebServlet(name = "EditDiaryController", urlPatterns = "/edit-diary-router")
public class EditDiaryController extends HttpServlet {

    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        int diaryId = Integer.parseInt(request.getParameter("id"));
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        HttpSession session = request.getSession();
        session.setAttribute("diaryId",diaryId);
        session.setAttribute("title",title);
        session.setAttribute("content",content);
        DefaultResponse defaultResponse = new DefaultResponse(true);
        response.getWriter().print(defaultResponse);
        System.out.println("<"+ LocalDateTime.now()+"> Store diary: \nResult: "+defaultResponse);
    }
}
