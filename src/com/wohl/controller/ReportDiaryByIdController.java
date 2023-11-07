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

@WebServlet(name = "ReportDiaryByIdController", urlPatterns = "/report-diary-by-id")
public class ReportDiaryByIdController extends HttpServlet {

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
        int idOfDiary = Integer.parseInt(request.getParameter("idOfDiary"));
        String reason = request.getParameter("reason");
        Diary diary = new Diary();
        diary.setId(idOfDiary);
        DiaryDao diaryDao = new DiaryDaoImpl();
        DefaultResponse defaultResponse = new DefaultResponse();
        if(diaryDao.report(diary,reason))
            defaultResponse.setResult(true);
        else
            defaultResponse.setResult(false);
        response.getWriter().print(defaultResponse);
        System.out.println("<" + LocalDateTime.now() + "> Report diary: \nResult: " + defaultResponse);
    }
}
