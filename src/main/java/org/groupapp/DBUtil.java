package org.groupapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.prefs.Preferences;

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

    public static boolean registerUserToActivityIfNotExists(String userId, int activityId) {
        String checkSql = "SELECT * FROM registration WHERE user_id = ? AND activity_id = ?";
        String insertSql = "INSERT INTO registration (user_id, activity_id) VALUES (?, ?)";

        try (Connection conn = getConnection();
                PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setString(1, userId);
            checkStmt.setInt(2, activityId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                System.out.println("⚠️ 使用者已報名此活動！");
                return false;
            }

            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setString(1, userId);
                insertStmt.setInt(2, activityId);
                insertStmt.executeUpdate();
                return true;
            }

        } catch (SQLException e) {
            System.out.println("❌ 報名失敗：" + e.getMessage());
            return false;
        }
    }

    public static java.util.List<Activity> getAllActivities() {
        java.util.List<Activity> list = new java.util.ArrayList<>();

        String sql = "SELECT id, name, date, time, place FROM activity";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Activity act = new Activity();
                act.setId(rs.getInt("id"));
                act.setName(rs.getString("name"));
                act.setDate(rs.getString("date"));
                act.setTime(rs.getString("time"));
                act.setPlace(rs.getString("place"));
                list.add(act);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static boolean addFavorite(String userId, int activityId) {
        String sql = "INSERT INTO favorite (user_id, activity_id) VALUES (?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userId);
            stmt.setInt(2, activityId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("收藏失敗：" + e.getMessage());
            return false;
        }
    }

    public static Set<Integer> getFavoriteActivityIds(String userId) {
        Set<Integer> favoriteIds = new HashSet<>();
        String sql = "SELECT activity_id FROM favorite WHERE user_id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                favoriteIds.add(rs.getInt("activity_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return favoriteIds;
    }

    public static boolean removeFavorite(String userId, int activityId) {
        String sql = "DELETE FROM favorite WHERE user_id = ? AND activity_id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userId);
            stmt.setInt(2, activityId);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("取消收藏失敗：" + e.getMessage());
            return false;
        }
    }

    public static Activity getActivityById(int activityId) {
        Activity act = null;
        String sql = "SELECT a.*, u.name AS host_name FROM activity a LEFT JOIN user u ON a.host_id = u.id WHERE a.id = ?";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, activityId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                act = new Activity();
                act.setId(rs.getInt("id"));
                act.setName(rs.getString("name"));
                act.setDate(rs.getString("date"));
                act.setTime(rs.getString("time"));
                act.setPlace(rs.getString("place"));
                act.setIntro(rs.getString("intro"));
                act.setDueDate(rs.getString("due_date"));
                act.setDueTime(rs.getString("due_time"));
                act.setHostName(rs.getString("host_name")); // ← 主辦人名稱
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return act;
    }

    public static List<Activity> getRegisteredActivities(String userId) {
        List<Activity> list = new ArrayList<>();
        String sql = "SELECT a.* FROM registration r JOIN activity a ON r.activity_id = a.id WHERE r.user_id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Activity act = new Activity();
                act.setId(rs.getInt("id"));
                act.setName(rs.getString("name"));
                act.setDate(rs.getString("date"));
                act.setTime(rs.getString("time"));
                act.setPlace(rs.getString("place"));
                act.setIntro(rs.getString("intro"));
                act.setDueDate(rs.getString("due_date"));
                act.setDueTime(rs.getString("due_time"));
                list.add(act);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static boolean cancelRegistration(String userId, int activityId) {
        String sql = "DELETE FROM registration WHERE user_id = ? AND activity_id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userId);
            stmt.setInt(2, activityId);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isUserRegistered(String userId, int activityId) {
        String sql = "SELECT 1 FROM registration WHERE user_id = ? AND activity_id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userId);
            stmt.setInt(2, activityId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean validateUser(String id, String password) {
        String sql = "SELECT * FROM user WHERE id = ? AND password = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean addActivity(Activity act) {
        String sql = "INSERT INTO activity (name, date, time, place, intro, due_date, due_time, host_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            // 從 Preferences 取得目前登入者（主辦人）ID
            Preferences prefs = Preferences.userRoot().node("org.groupapp");
            String hostId = prefs.get("userId", null);

            if (hostId == null) {
                System.out.println("⚠️ 尚未登入，無法新增活動");
                return false;
            }

            // 設定參數
            stmt.setString(1, act.getName());
            stmt.setString(2, act.getDate());
            stmt.setString(3, act.getTime());
            stmt.setString(4, act.getPlace());
            stmt.setString(5, act.getIntro());
            stmt.setString(6, act.getDueDate());
            stmt.setString(7, act.getDueTime());
            stmt.setString(8, hostId);

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
