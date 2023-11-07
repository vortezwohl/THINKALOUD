package com.wohl.misc.util;

import lombok.SneakyThrows;
import java.sql.*;

public class MySQLUtil {
    @SneakyThrows
    public static Connection connect(String db, String user, String pass) {
        Class.forName("com.mysql.cj.jdbc.Driver");
        StringBuilder url = new StringBuilder();
        url.append("jdbc:mysql://localhost:3306/").append(db).append("?serverTimezone=GMT%2B8");
        return DriverManager.getConnection(url.toString(), user, pass);
    }
    @SneakyThrows
    public static void rel(Connection conn){
        if(conn != null)
            conn.close();
    }
    @SneakyThrows
    public static void rel(PreparedStatement pstmt, Connection conn) {
        if (pstmt != null)
            pstmt.close();
        if (conn != null)
            conn.close();
    }
    @SneakyThrows
    public static void rel(ResultSet rs, PreparedStatement pstmt, Connection conn) {
        rel(pstmt, conn);
        if (rs != null)
            rs.close();
    }
    @SneakyThrows
    public static void rel(Statement stmt, Connection conn) {
        if (stmt != null)
            stmt.close();
        if (conn != null)
            conn.close();
    }
    @SneakyThrows
    public static void rel(ResultSet rs, Statement stmt, Connection conn) {
        rel(stmt, conn);
        if (rs != null)
            rs.close();
    }
    @SneakyThrows
    public static void rel(ResultSet rs, Statement stmt) {
        rel(stmt);
        if (rs != null)
            rs.close();
    }
    @SneakyThrows
    public static void rel(ResultSet rs, PreparedStatement pstmt) {
        rel(pstmt);
        if (rs != null)
            rs.close();
    }
    @SneakyThrows
    public static void rel(Statement stmt) {
        if (stmt != null)
            stmt.close();
    }
    @SneakyThrows
    public static void rel(PreparedStatement pstmt) {
        if (pstmt != null)
            pstmt.close();
    }
}
