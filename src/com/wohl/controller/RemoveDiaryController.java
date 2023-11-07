package com.wohl.controller;

import com.wohl.dao.impl.CommentDaoImpl;
import com.wohl.dao.impl.DiaryDaoImpl;
import com.wohl.dao.intf.CommentDao;
import com.wohl.dao.intf.DiaryDao;
import com.wohl.entity.Diary;
import com.wohl.misc.DefaultResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;

import java.time.LocalDateTime;

@WebServlet(name = "RemoveDiaryController", urlPatterns = "/remove-diary")
public class RemoveDiaryController extends HttpServlet {

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        int idOfDiary = Integer.parseInt(request.getParameter("idOfDiary"));
        Diary diary = new Diary();
        diary.setId(idOfDiary);
        DiaryDao diaryDao = new DiaryDaoImpl();
        CommentDao commentDao = new CommentDaoImpl();
        DefaultResponse defaultResponse = new DefaultResponse();

        if(commentDao.removeCommentsUnderDiary(diary) && diaryDao.removeDiary(diary))
            defaultResponse.setResult(true);
        else
            defaultResponse.setResult(false);
        response.getWriter().print(defaultResponse);
        System.out.println("<"+ LocalDateTime.now()+"> Remove diary: \nResult: "+defaultResponse);
    }
}
