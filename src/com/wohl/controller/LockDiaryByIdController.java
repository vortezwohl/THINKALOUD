package com.wohl.controller;

import com.wohl.dao.impl.DiaryDaoImpl;
import com.wohl.dao.intf.DiaryDao;
import com.wohl.entity.Diary;
import com.wohl.misc.DefaultResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;

import java.time.LocalDateTime;

@WebServlet(name = "LockDiaryByIdController", urlPatterns = "/lock-diary-by-id")
public class LockDiaryByIdController extends HttpServlet {

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        DiaryDao diaryDao = new DiaryDaoImpl();
        int diaryId = Integer.parseInt(request.getParameter("diaryId"));
        Diary diary = diaryDao.fetchDiaryById(diaryId);
        DefaultResponse defaultResponse = new DefaultResponse();
        if(diary == null)
            defaultResponse.setResult(false);
        else {
            diaryDao.lockDiary(diary);
            defaultResponse.setResult(true);
        }
        response.getWriter().print(defaultResponse);
        System.out.println("<"+ LocalDateTime.now()+"> Lock diary: \nResult: "+defaultResponse);
    }
}
