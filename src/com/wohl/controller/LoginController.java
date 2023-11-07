package com.wohl.controller;

import com.wohl.dao.impl.UserDaoImpl;
import com.wohl.dao.intf.UserDao;
import com.wohl.entity.User;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import java.time.LocalDateTime;

@WebServlet(name = "LoginController", urlPatterns = "/login")
public class LoginController extends HttpServlet {

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
        boolean loggedIn = false;
        UserDao userDao = new UserDaoImpl();
        User user = new User();
        User currentUser = new User();
        user.setAccount(request.getParameter("user"));
        user.setPassword(request.getParameter("pass"));
        /*-2: user banned -1: user not found, 0: user found, 1: user is admin*/
        switch (userDao.findUser(user)){
            case 1:
            case 0:
                currentUser = userDao.selectUserByAccount(user.getAccount());
                break;
            case -1:
                userDao.addUser(user);
                currentUser = userDao.selectUserByAccount(user.getAccount());
                System.out.println("<"+LocalDateTime.now()+"> Registered: "+currentUser);
                break;
            case -2:
                user.setBanned(1);
                currentUser = userDao.selectUserByAccount(user.getAccount());
                break;
            default:
        }
        response.getWriter().print(currentUser);
        request.getSession().setAttribute("user",currentUser);
        System.out.println("<"+LocalDateTime.now()+"> User reached: \nUser: "+currentUser);
    }
}
