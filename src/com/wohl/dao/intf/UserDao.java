package com.wohl.dao.intf;

import com.wohl.entity.User;

import java.util.ArrayList;

public interface UserDao {
    public abstract boolean addUser(User user);
    public abstract boolean removeUser(User user);
    public abstract boolean banUser(User user);
    public abstract boolean unbanUser(User user);
    public abstract int findUser(User user);
    public abstract User selectUserById(int id);
    public abstract User selectUserByAccount(String account);
    public abstract ArrayList<User> listAll();
}
