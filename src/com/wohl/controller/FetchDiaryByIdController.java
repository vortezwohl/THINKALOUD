package com.wohl.controller;

import com.wohl.dao.impl.DiaryDaoImpl;
import com.wohl.dao.intf.DiaryDao;
import com.wohl.entity.Diary;
import com.wohl.misc.DefaultResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

@WebServlet(name = "FetchDiaryByIdController", urlPatterns = "/fetch-diary-by-id")
public class FetchDiaryByIdController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        int diaryId =  Integer.parseInt(request.getParameter("diaryId"));
        DiaryDao diaryDao = new DiaryDaoImpl();
        Diary thatOneDiary = diaryDao.fetchDiaryById(diaryId);
        try {
            if(thatOneDiary != null) {
                response.getWriter().print(thatOneDiary);
                System.out.println("<" + LocalDateTime.now() + "> Fetch diary by diaryId: \nDiary: " + thatOneDiary);
            }
            else {
                response.getWriter().print(new DefaultResponse(true));
                System.out.println("<" + LocalDateTime.now() + "> Failed to Fetch diary by diaryId: \nResult: " + new DefaultResponse(false));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}