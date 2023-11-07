package com.wohl.dao.impl;

import com.wohl.config.Data;
import com.wohl.dao.intf.DiaryDao;
import com.wohl.misc.util.MySQLUtil;
import com.wohl.entity.Diary;
import com.wohl.entity.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DiaryDaoImpl implements DiaryDao {

    @Override
    public boolean addDiary(Diary diary) {
        try {
            PreparedStatement pstmt = Data.io.prepareStatement("insert into diary(title, content, datetime, private, idOfWriter, accountOfWriter) values(?,?,?,?,?,?)");
            pstmt.setString(1, diary.getTitle());
            pstmt.setString(2, diary.getContent());
            pstmt.setString(3, diary.getDatetime());
            pstmt.setInt(4, diary.get_private());
            pstmt.setInt(5, diary.getIdOfWriter());
            pstmt.setString(6, diary.getAccountOfWriter());
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
    public boolean removeDiary(Diary diary) {
        try {
            PreparedStatement pstmt = Data.io.prepareStatement("delete from diary where id=?");
            pstmt.setInt(1, diary.getId());
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
    public boolean editDiary(Diary diary, String newTitle, String newContent, String newDatetime, int newPrivacy) {
        try {
            PreparedStatement pstmt = Data.io.prepareStatement("update diary set title=?,content=?,datetime=?,private=? where id=?");
            pstmt.setString(1,newTitle);
            pstmt.setString(2,newContent);
            pstmt.setString(3,newDatetime);
            pstmt.setInt(4,newPrivacy);
            pstmt.setInt(5,diary.getId());
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
    public boolean report(Diary diary, String reason) {
        try {
            PreparedStatement pstmt = Data.io.prepareStatement("update diary set reported=1,reportReason=? where id=?");
            pstmt.setString(1,reason);
            pstmt.setInt(2, diary.getId());
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
    public boolean lockDiary(Diary diary) {
        try {
            PreparedStatement pstmt = Data.io.prepareStatement("update diary set locked=1 where id=?");
            pstmt.setInt(1, diary.getId());
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
    public boolean unlockDiary(Diary diary) {
        try {
            PreparedStatement pstmt = Data.io.prepareStatement("update diary set locked=0 where id=?");
            pstmt.setInt(1, diary.getId());
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
    public ArrayList<Diary> listAll() {
        ArrayList<Diary> result = new ArrayList<>();
        try {
            Statement stmt = Data.io.createStatement();
            ResultSet resultSet =  stmt.executeQuery("select * from diary order by datetime asc");
            while(resultSet.next()){
                result.add(
                        new Diary(
                                resultSet.getInt("id"),
                                resultSet.getInt("private"),
                                resultSet.getInt("locked"),
                                resultSet.getInt("reported"),
                                resultSet.getInt("idOfWriter"),
                                resultSet.getString("title"),
                                resultSet.getString("content"),
                                resultSet.getString("datetime"),
                                resultSet.getString("accountOfWriter"),
                                resultSet.getString("reportReason")
                                )
                );
            }
            MySQLUtil.rel(resultSet,stmt);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<Diary> listAllFromUser(User user) {
        ArrayList<Diary> result = new ArrayList<>();
        try {
            PreparedStatement pstmt = Data.io.prepareStatement("select * from diary where idOfWriter=? order by datetime asc");
            pstmt.setInt(1,user.getId());
            ResultSet resultSet = pstmt.executeQuery();
            while(resultSet.next()){
                result.add(
                        new Diary(
                                resultSet.getInt("id"),
                                resultSet.getInt("private"),
                                resultSet.getInt("locked"),
                                resultSet.getInt("reported"),
                                resultSet.getInt("idOfWriter"),
                                resultSet.getString("title"),
                                resultSet.getString("content"),
                                resultSet.getString("datetime"),
                                resultSet.getString("accountOfWriter"),
                                resultSet.getString("reportReason")
                        )
                );
            }
            MySQLUtil.rel(resultSet,pstmt);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<Diary> listAllFromPublicExcludeUser(User user) {
        ArrayList<Diary> result = new ArrayList<>();
        try {
            Statement stmt = Data.io.createStatement();
            ResultSet resultSet = stmt.executeQuery("select * from diary where private=0 and locked=0 order by datetime asc");
            while(resultSet.next()){
                if(resultSet.getInt("idOfWriter") != user.getId()) {
                    result.add(
                            new Diary(
                                    resultSet.getInt("id"),
                                    resultSet.getInt("private"),
                                    resultSet.getInt("locked"),
                                    resultSet.getInt("reported"),
                                    resultSet.getInt("idOfWriter"),
                                    resultSet.getString("title"),
                                    resultSet.getString("content"),
                                    resultSet.getString("datetime"),
                                    resultSet.getString("accountOfWriter"),
                                    resultSet.getString("reportReason")
                            )
                    );
                }
            }
            MySQLUtil.rel(resultSet,stmt);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Diary fetchDiaryById(int id) {
        Diary result = null;
        try {
            PreparedStatement pstmt = Data.io.prepareStatement("select * from diary where id=?");
            pstmt.setInt(1,id);
            ResultSet resultSet = pstmt.executeQuery();
            while(resultSet.next()){
                result = new Diary(
                                resultSet.getInt("id"),
                                resultSet.getInt("private"),
                                resultSet.getInt("locked"),
                                resultSet.getInt("reported"),
                                resultSet.getInt("idOfWriter"),
                                resultSet.getString("title"),
                                resultSet.getString("content"),
                                resultSet.getString("datetime"),
                                resultSet.getString("accountOfWriter"),
                                resultSet.getString("reportReason")
                        );
            }
            MySQLUtil.rel(resultSet,pstmt);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}