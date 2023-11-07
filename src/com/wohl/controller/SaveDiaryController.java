package com.wohl.controller;

import com.wohl.dao.impl.DiaryDaoImpl;
import com.wohl.dao.impl.UserDaoImpl;
import com.wohl.dao.intf.DiaryDao;
import com.wohl.dao.intf.UserDao;
import com.wohl.entity.Diary;
import com.wohl.entity.User;
import com.wohl.misc.DefaultResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet(name = "WriteDiaryController", urlPatterns = "/save-diary")
public class SaveDiaryController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response){

        String datetime = request.getParameter("datetime");
        int privacy = Integer.parseInt(request.getParameter("privacy"));
        int writerId = Integer.parseInt(request.getParameter("writerId"));
        String title = request.getParameter("title");
        String content = request.getParameter("content");

        UserDao userDao = new UserDaoImpl();
        User writer = userDao.selectUserById(writerId);

        Diary diary = new Diary();
        diary.setTitle(title);
        diary.setContent(content);
        diary.setDatetime(datetime);
        diary.set_private(privacy);
        diary.setIdOfWriter(writerId);
        diary.setAccountOfWriter(writer.getAccount());

        DefaultResponse defaultResponse;
        DiaryDao diaryDao = new DiaryDaoImpl();
        if(diaryDao.addDiary(diary))
            defaultResponse = new DefaultResponse(true);
        else
            defaultResponse = new DefaultResponse(false);
        try {
            response.getWriter().print(defaultResponse);
            System.out.println("<"+ LocalDateTime.now()+"> Save diary: \nResult: "+defaultResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
