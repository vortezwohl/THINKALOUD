package com.wohl.controller;

import com.wohl.dao.impl.DiaryDaoImpl;
import com.wohl.dao.intf.DiaryDao;
import com.wohl.entity.Diary;
import com.wohl.misc.DefaultResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;

import java.time.LocalDateTime;

@WebServlet(name = "SaveEditedDiaryController", urlPatterns = "/save-edited-diary")
public class SaveEditedDiaryController extends HttpServlet {

    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response){

        HttpSession session = request.getSession();
        session.removeAttribute("diaryId");
        session.removeAttribute("title");
        session.removeAttribute("content");
        int diaryId = Integer.parseInt(request.getParameter("diaryId"));
        int privacy = Integer.parseInt(request.getParameter("privacy"));
        String datetime = request.getParameter("datetime");
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        DiaryDao diaryDao = new DiaryDaoImpl();
        Diary diary = new Diary();
        diary.setId(diaryId);
        DefaultResponse defaultResponse = new DefaultResponse();
        if(diaryDao.editDiary(diary,title,content,datetime,privacy))
            defaultResponse.setResult(true);
        else
            defaultResponse.setResult(false);
        response.getWriter().print(defaultResponse);
        System.out.println("<"+ LocalDateTime.now()+"> Edit diary: \nResult: "+defaultResponse);
    }
}
