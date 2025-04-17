package org.groupapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    public static java.util.List<String> searchActivitiesByName(String keyword) {
        java.util.List<String> results = new java.util.ArrayList<>();

        try (Connection conn = getConnection()) {
            String sql = "SELECT name, date, time, place FROM activity WHERE name LIKE ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + keyword + "%");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String info = String.format(
                        "活動：%s｜時間：%s %s｜地點：%s",
                        rs.getString("name"),
                        rs.getString("date"),
                        rs.getString("time"),
                        rs.getString("place"));
                results.add(info);
            }

        } catch (Exception e) {
            results.add(" 查詢錯誤：" + e.getMessage());
        }

        return results;
    }

}
