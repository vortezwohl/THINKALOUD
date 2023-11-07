package com.wohl.dao.impl;

import com.wohl.config.Data;
import com.wohl.dao.intf.UserDao;
import com.wohl.misc.util.MySQLUtil;
import com.wohl.entity.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class UserDaoImpl implements UserDao {

    @Override
    public boolean addUser(User user) {
        try {
            PreparedStatement pstmt = Data.io.prepareStatement("insert into user(isAdmin,account,password) values(?,?,?)");
            pstmt.setInt(1, user.getIsAdmin());
            pstmt.setString(2,user.getAccount());
            pstmt.setString(3,user.getPassword());
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

    //TODO unused method
    @Override
    public boolean removeUser(User user) {
        return false;
    }

    /**-2: user banned -1: user not found, 0: user found, 1: user is admin*/
    @Override
    public int findUser(User user) {
        try {
            PreparedStatement pstmt = Data.io.prepareStatement("select * from user where account=? and password=?");
            pstmt.setString(1,user.getAccount());
            pstmt.setString(2,user.getPassword());
            ResultSet res = pstmt.executeQuery();
            if(res.next()) {
                if(res.getInt("banned") == 1)
                    return -2;
                else
                    return res.getInt("isAdmin");
            }
            MySQLUtil.rel(res,pstmt);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public User selectUserById(int id) {
        User result = null;
        try {
            PreparedStatement pstmt = Data.io.prepareStatement("select * from user where id=?");
            pstmt.setInt(1,id);
            ResultSet resultSet = pstmt.executeQuery();
            if(resultSet.next()){
                result = new User();
                result.setId(id);
                result.setBanned(resultSet.getInt("banned"));
                result.setIsAdmin(resultSet.getInt("isAdmin"));
                result.setAccount(resultSet.getString("account"));
                result.setPassword(resultSet.getString("password"));
            }
            MySQLUtil.rel(resultSet,pstmt);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public User selectUserByAccount(String account) {
        User result = null;
        try {
            PreparedStatement pstmt = Data.io.prepareStatement("select * from user where account=?");
            pstmt.setString(1,account);
            ResultSet resultSet = pstmt.executeQuery();
            if(resultSet.next()){
                result = new User();
                result.setId(resultSet.getInt("id"));
                result.setBanned(resultSet.getInt("banned"));
                result.setIsAdmin(resultSet.getInt("isAdmin"));
                result.setAccount(account);
                result.setPassword(resultSet.getString("password"));
            }
            MySQLUtil.rel(resultSet,pstmt);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean banUser(User user) {
        try {
            PreparedStatement pstmt = Data.io.prepareStatement("update user set banned=1 where id=?");
            pstmt.setInt(1, user.getId());
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
    public boolean unbanUser(User user) {
        try {
            PreparedStatement pstmt = Data.io.prepareStatement("update user set banned=0 where id=?");
            pstmt.setInt(1, user.getId());
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
    public ArrayList<User> listAll() {
        ArrayList<User> result = new ArrayList<>();
        try {
            Statement stmt = Data.io.createStatement();
            ResultSet resultSet = stmt.executeQuery("select * from user");
            while(resultSet.next()){
                result.add(new User(
                        resultSet.getInt("id"),
                        resultSet.getInt("banned"),
                        resultSet.getInt("isAdmin"),
                        resultSet.getString("account"),
                        resultSet.getString("password")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
