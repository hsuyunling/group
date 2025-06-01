package org.groupapp;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
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

import javax.swing.JOptionPane;

public class DBUtil {
    private static final String SERVER = "jdbc:mysql://140.119.19.73:3315/";
    private static final String DATABASE = "TG13"; // 你的資料庫名稱
    private static DataSource dataSource;

    static {
        try {
            // 顯式加載 MySQL 驅動
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL 驅動加載成功");
            
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(SERVER + DATABASE + "?useSSL=false");
            config.setUsername("TG13");
            config.setPassword("pS7auF");
            config.setMaximumPoolSize(10);
            dataSource = new HikariDataSource(config);
            System.out.println("數據庫連接池初始化成功");
        } catch (ClassNotFoundException e) {
            System.err.println("找不到 MySQL 驅動程序：" + e.getMessage());
            throw new RuntimeException("找不到 MySQL 驅動程序", e);
        } catch (Exception e) {
            System.err.println("數據庫連接池初始化失敗：" + e.getMessage());
            throw new RuntimeException("數據庫連接池初始化失敗", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
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

    // 註冊時必要資料的輸入
    public void execute(String name, String email, String phone, String number, String pass) {
        // try-with 確保關掉
        try (Connection conn = getConnection()) {
            System.out.println("DB Connected");
            String query = "INSERT INTO `user`(id, password, name, email, phone) VALUES(?, ?, ?, ?, ?)";

            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, number);
                pstmt.setString(2, pass);
                pstmt.setString(3, name);
                pstmt.setString(4, email);
                pstmt.setString(5, phone);
                pstmt.executeUpdate();
                System.out.println("成功喔！");
            }
        } catch (SQLException e) {
            System.out.println("資料庫錯誤：" + e.getMessage());
        }
    }

    // 輸入非必要資料，性別
    public boolean execute(String gender, String name) {
        // try-with 確保關掉
        try (Connection conn = getConnection()) {
            System.out.println("DB Connected");
            String query = "UPDATE `user` SET gender = ? WHERE name = ?;";

            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, gender);
                pstmt.setString(2, name);
                pstmt.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            System.out.println("資料庫錯誤：" + e.getMessage());
            return false;
        }
    }

    boolean success = false;

    // 保留你原來的 select 方法做向後兼容
    public User select(String id, String pass) {
        User user = authenticateUser(id, pass);
        this.success = (user != null);
        return user;
    }

    // 改進的 select 方法 - 使用原來的結構但優化效能
    public static User authenticateUser(String id, String password) {
        // 輸入驗證
        if (id == null || id.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return null;
        }

        String query = "SELECT id, name, email, phone, gender FROM user WHERE id = ? AND password = ?";

        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            System.out.println("正在驗證使用者：" + id);

            pstmt.setString(1, id.trim());
            pstmt.setString(2, password.trim());

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // 建立 User 物件
                    User user = new User(
                            rs.getString("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getString("gender"));

                    System.out.println("使用者驗證成功：" + user.getName());
                    return user;
                } else {
                    System.out.println("使用者驗證失敗：帳號或密碼錯誤");
                    return null;
                }
            }
        } catch (SQLException e) {
            System.err.println("資料庫連接錯誤：" + e.getMessage());
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            System.err.println("未預期的錯誤：" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // 檢查成功登入與否
    public boolean getSuccess() {
        return success;
    }

    // 編輯個人資訊
    public boolean updateUser(User user) {
        try (Connection conn = getConnection()) {
            String query = "UPDATE `user` SET name = ?, email = ?, phone = ?, gender = ? WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, user.getName());
                pstmt.setString(2, user.getEmail());
                pstmt.setString(3, user.getPhone());
                pstmt.setString(4, user.getGender());
                pstmt.setString(5, user.getId()); // id 當作唯一辨識
                pstmt.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            System.out.println("資料庫錯誤：" + e.getMessage());
            return false;
        }
    }

    // 檢查學號是否已註冊
    public boolean isNumberExists(String number) {
        try (Connection conn = getConnection()) {
            String query = "SELECT COUNT(*) FROM user WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, number);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean addActivity(Activity act) {
        String sql = "INSERT INTO activity (name, date, time, place, intro, due_date, due_time, host_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            // 從 Preferences 取得目前登入者（主辦人）ID
            Preferences prefs = Preferences.userRoot().node("org.groupapp");
            String hostId = prefs.get("userId", null);

            if (hostId == null) {
                JOptionPane.showMessageDialog(null, "請先登入才能新增活動", "未登入", JOptionPane.WARNING_MESSAGE);
                return false;
            }

            // 必填欄位檢查
            if (act.getName().isEmpty() || act.getDate().isEmpty() || act.getTime().isEmpty()) {
                JOptionPane.showMessageDialog(null, "請填寫活動名稱、日期和時間", "資料不完整", JOptionPane.WARNING_MESSAGE);
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
            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "活動新增成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "活動新增失敗，請稍後再試", "失敗", JOptionPane.ERROR_MESSAGE);
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "資料庫錯誤：" + e.getMessage(), "錯誤", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

}
