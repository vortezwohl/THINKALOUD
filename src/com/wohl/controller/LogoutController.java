package com.wohl.controller;

import com.wohl.dao.impl.UserDaoImpl;
import com.wohl.dao.intf.UserDao;
import com.wohl.entity.User;
import com.wohl.misc.DefaultResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;

import java.time.LocalDateTime;

@WebServlet(name = "LogoutController", urlPatterns = "/logout")
public class LogoutController extends HttpServlet {

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
        int uid = Integer.parseInt(request.getParameter("uid"));
        UserDao userDao = new UserDaoImpl();
        User user = userDao.selectUserById(uid);
        HttpSession session = request.getSession();
        session.removeAttribute("user");
        DefaultResponse defaultResponse = new DefaultResponse(true);
        response.getWriter().print(defaultResponse);
        System.out.println("<"+ LocalDateTime.now()+"> User went Offline: \nUser: "+user);
    }
}
