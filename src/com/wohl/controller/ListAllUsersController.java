package com.wohl.controller;

import com.wohl.dao.impl.UserDaoImpl;
import com.wohl.dao.intf.UserDao;
import com.wohl.entity.User;
import com.wohl.misc.util.JSONList;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;

import java.time.LocalDateTime;

@WebServlet(name = "ListAllUsersController", urlPatterns = "/list-all-users")
public class ListAllUsersController extends HttpServlet {

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        UserDao userDao = new UserDaoImpl();
        JSONList<User> userJSONList = new JSONList<>(userDao.listAll());
        response.getWriter().print(userJSONList);
        System.out.println("<"+ LocalDateTime.now()+"> List all users:");
        for(User aUser : userDao.listAll())
            System.out.println("User: "+aUser);
    }
}
