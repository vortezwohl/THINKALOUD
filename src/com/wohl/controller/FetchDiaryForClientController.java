package com.wohl.controller;

import com.wohl.dao.impl.DiaryDaoImpl;
import com.wohl.dao.intf.DiaryDao;
import com.wohl.entity.Diary;
import com.wohl.misc.util.JSONList;
import com.wohl.entity.User;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

@WebServlet(name = "FetchDiaryForClientController", urlPatterns = "/fetch-diary-client")
public class FetchDiaryForClientController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
        DiaryDao diaryDao = new DiaryDaoImpl();
        User user = new User();
        user.setId(Integer.parseInt(request.getParameter("userId")));
        ArrayList<Diary> diaryArrayList = diaryDao.listAllFromUser(user);
        diaryArrayList.addAll(diaryDao.listAllFromPublicExcludeUser(user));
        JSONList<Diary> jsonList = new JSONList<>(diaryArrayList);
        try {
            response.getWriter().print(jsonList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("<"+ LocalDateTime.now()+"> Fetch all public diaries: ");
        for(Diary diary : diaryArrayList)
            System.out.println("Diary: "+diary);
    }
}
