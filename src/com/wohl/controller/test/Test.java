package com.wohl.controller.test;

import com.wohl.dao.impl.DiaryDaoImpl;
import com.wohl.dao.impl.UserDaoImpl;
import com.wohl.dao.intf.DiaryDao;
import com.wohl.dao.intf.UserDao;
import com.wohl.entity.Diary;
import com.wohl.entity.User;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "Test", urlPatterns = "/test")
public class Test extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
        //TODO test on UserDao
//        User user = new User();
//        user.setAccount(request.getParameter("account"));
//        user.setPassword(request.getParameter("password"));
//        UserDao userDao = new UserDaoImpl();
//        userDao.addUser(user);
//        System.out.println(user);

        //TODO test on DiaryDao
//        Diary diary = new Diary();
//        diary.setTitle("Test on DiaryDao");
//        diary.setContent("This is the first test on DiaryDao");
//        diary.setDatetime("2023-10-31 19:51:04");
//        DiaryDao diaryDao = new DiaryDaoImpl();
//        diaryDao.addDiary(diary);
//        System.out.println(diary);
    }
}