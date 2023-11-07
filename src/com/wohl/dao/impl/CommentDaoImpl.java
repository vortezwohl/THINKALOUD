package com.wohl.dao.impl;

import com.wohl.config.Data;
import com.wohl.dao.intf.CommentDao;
import com.wohl.dao.intf.UserDao;
import com.wohl.misc.util.MySQLUtil;
import com.wohl.entity.Comment;
import com.wohl.entity.Diary;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CommentDaoImpl implements CommentDao {

    @Override
    public boolean addComment(Diary diary, Comment comment) {
        try {
            UserDao userDao = new UserDaoImpl();
            PreparedStatement pstmt = Data.io.prepareStatement("insert into comment(idOfDiary, content, datetime, idOfWriter, accountOfWriter) values(?,?,?,?,?)");
            pstmt.setInt(1, diary.getId());
            pstmt.setString(2, comment.getContent());
            pstmt.setString(3,comment.getDatetime());
            pstmt.setInt(4,comment.getIdOfWriter());
            pstmt.setString(5,userDao.selectUserById(comment.getIdOfWriter()).getAccount());
            if(pstmt.executeUpdate() > 0) {
                MySQLUtil.rel(pstmt);
                return true;
            }
            else MySQLUtil.rel(pstmt);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean removeComment(Comment comment) {
        try {
            PreparedStatement pstmt = Data.io.prepareStatement("delete from comment where id=?");
            pstmt.setInt(1, comment.getId());
            if(pstmt.executeUpdate() > 0) {
                MySQLUtil.rel(pstmt);
                return true;
            }
            else MySQLUtil.rel(pstmt);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public ArrayList<Comment> listAllFrom(Diary diary) {
        ArrayList<Comment> result = new ArrayList<>();
        try {
            PreparedStatement pstmt = Data.io.prepareStatement("select * from comment where idOfDiary=? order by datetime asc");
            pstmt.setInt(1, diary.getId());
            ResultSet resultSet = pstmt.executeQuery();
            while(resultSet.next()){
                result.add(
                        new Comment(
                                resultSet.getInt("idOfDiary"),
                                resultSet.getInt("idOfAnotherComment"),
                                resultSet.getInt("id"),
                                resultSet.getInt("reported"),
                                resultSet.getInt("idOfWriter"),
                                resultSet.getString("accountOfWriter"),
                                resultSet.getString("content"),
                                resultSet.getString("datetime")
                        )
                );
            }
            MySQLUtil.rel(resultSet,pstmt);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean removeCommentsUnderDiary(Diary diary) {
        boolean result = true;
        try {
            PreparedStatement pstmt = Data.io.prepareStatement("delete from comment where idOfDiary=?");
            pstmt.setInt(1, diary.getId());
            if(pstmt.executeUpdate() > 0) {
                PreparedStatement pstmt_1 = Data.io.prepareStatement("select * from comment where idOfDiary=?");
                pstmt_1.setInt(1,diary.getId());
                if(pstmt_1.executeQuery().next())
                    result = false;
                MySQLUtil.rel(pstmt);
                MySQLUtil.rel(pstmt_1);
            }
            else MySQLUtil.rel(pstmt);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
