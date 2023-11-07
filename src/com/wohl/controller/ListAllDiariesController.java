package com.wohl.controller;

import com.wohl.dao.impl.DiaryDaoImpl;
import com.wohl.dao.intf.DiaryDao;
import com.wohl.entity.Diary;
import com.wohl.entity.User;
import com.wohl.misc.util.JSONList;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;

import java.time.LocalDateTime;
import java.util.ArrayList;

@WebServlet(name = "ListAllDiariesController", urlPatterns = "/list-all-diaries")
public class ListAllDiariesController extends HttpServlet {

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        DiaryDao diaryDao = new DiaryDaoImpl();
        JSONList<Diary> diaryJSONList = new JSONList<>(diaryDao.listAll());
        response.getWriter().print(diaryJSONList);
        System.out.println("<"+ LocalDateTime.now()+"> List all users:");
        for(Diary aDiary : diaryDao.listAll())
            System.out.println("User: "+aDiary);
    }
}
