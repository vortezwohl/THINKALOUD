package com.wohl.config;

import com.wohl.misc.util.MySQLUtil;
import lombok.SneakyThrows;
import java.sql.Connection;

public class Data {
    /**
         TODO: Configure your DBMS connection parameters in this class.
         user: [your username to dbms]
         pass: [your password to dbms]
         db: [database you want to get access to]
         io: the Connection object you can use to create sql statement
     */
    private static String user = "wohl";
    private static String pass = "Zh200386";
    private static String db = "diary_space";

    public static Connection io = MySQLUtil.connect(db, user, pass);
    public static void revive(){
        io = MySQLUtil.connect(db, user, pass);
    }
    @SneakyThrows
    public static void kill(){
        MySQLUtil.rel(io);
    }
}
