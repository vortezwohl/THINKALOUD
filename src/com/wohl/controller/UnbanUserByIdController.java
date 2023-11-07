package com.wohl.controller;

import com.wohl.dao.impl.UserDaoImpl;
import com.wohl.dao.intf.UserDao;
import com.wohl.entity.User;
import com.wohl.misc.DefaultResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import java.time.LocalDateTime;

@WebServlet(name = "UnanUserByIdController", urlPatterns = "/unban-user-by-id")
public class UnbanUserByIdController extends HttpServlet {

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        UserDao userDao = new UserDaoImpl();
        int userId = Integer.parseInt(request.getParameter("userId"));
        User user = userDao.selectUserById(userId);
        DefaultResponse defaultResponse = new DefaultResponse();
        if(user == null)
            defaultResponse.setResult(false);
        else {
            userDao.unbanUser(user);
            defaultResponse.setResult(true);
        }
        response.getWriter().print(defaultResponse);
        System.out.println("<"+ LocalDateTime.now()+"> Unban user: \nResult: "+defaultResponse);
    }
}
