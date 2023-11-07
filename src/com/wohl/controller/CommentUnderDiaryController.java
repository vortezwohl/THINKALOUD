package com.wohl.controller;

import com.wohl.dao.impl.CommentDaoImpl;
import com.wohl.dao.impl.DiaryDaoImpl;
import com.wohl.dao.intf.CommentDao;
import com.wohl.dao.intf.DiaryDao;
import com.wohl.entity.Comment;
import com.wohl.entity.Diary;
import com.wohl.misc.DefaultResponse;
import com.wohl.misc.util.JSONList;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import java.time.LocalDateTime;
import java.util.ArrayList;

@WebServlet(name = "CommentUnderDiaryController", urlPatterns = "/comment-under-diary")
public class CommentUnderDiaryController extends HttpServlet {

    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

        int idOfDiary = Integer.parseInt(request.getParameter("idOfDiary"));
        int idOfWriter = Integer.parseInt(request.getParameter("idOfWriter"));
        String content = request.getParameter("content");
        String datetime = request.getParameter("datetime");
        DiaryDao diaryDao = new DiaryDaoImpl();
        Comment comment = new Comment();
        CommentDao commentDao = new CommentDaoImpl();
        DefaultResponse defaultResponse = new DefaultResponse();

        Diary diary = diaryDao.fetchDiaryById(idOfDiary);
        comment.setContent(content);
        comment.setDatetime(datetime);
        comment.setIdOfWriter(idOfWriter);
        if (commentDao.addComment(diary, comment))
            defaultResponse.setResult(true);
        else
            defaultResponse.setResult(false);
        response.getWriter().print(defaultResponse);
        System.out.println("<" + LocalDateTime.now() + "> Make comment: \nResult: " + defaultResponse);
    }

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

        int idOfDiary = Integer.parseInt(request.getParameter("idOfDiary"));
        Diary diary = new Diary();
        CommentDao commentDao = new CommentDaoImpl();

        diary.setId(idOfDiary);
        ArrayList<Comment> commentArrayList = commentDao.listAllFrom(diary);
        JSONList<Comment> commentJSONList = new JSONList<>(commentArrayList);
        response.getWriter().print(commentJSONList);
        for(Comment showJsonComment : commentArrayList)
            System.out.println("<" + LocalDateTime.now() + "> Get comments: \nComment: " + showJsonComment);
    }
}
