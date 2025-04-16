package org.groupapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBUtil {
    private static final String SERVER = "jdbc:mysql://140.119.19.73:3315/";
    private static final String DATABASE = "TG13"; // 你的資料庫名稱
    private static final String URL = SERVER + DATABASE + "?useSSL=false";
    private static final String USERNAME = "TG13"; // 你的帳號
    private static final String PASSWORD = "pS7auF"; // 你的密碼

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
