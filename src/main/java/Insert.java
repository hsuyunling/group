import java.sql.*;

public class Insert {

    public Insert(String name, String email, String phone, String number, String address) {
        Execute(name, email, phone, number, address);
    }

    public Insert() {

    }

    // 連接資料庫
    public Connection getConnection() throws ClassNotFoundException, SQLException {

        // 連接
        String server = "jdbc:mysql://140.119.19.73:3315/";
        String database = "TG13"; // 資料庫名稱
        String url = server + database + "?useSSL=false";
        String username = "TG13"; // 我的帳號
        String password = "pS7auF"; // 我的密碼

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection(url, username, password);
        return conn;
    }

    public void Execute(String name, String email, String phone, String number, String pass) {
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

        } catch (ClassNotFoundException e) {
            System.out.println("找不到 JDBC Driver：" + e.getMessage());

        } catch (SQLException e) {
            System.out.println("資料庫錯誤：" + e.getMessage());
        }
    }

    // 檢查登入帳密是否正確
    public String select(String id, String pass) {
        try (Connection conn = getConnection()) {
            System.out.println("DB Connected");

            String query = "SELECT * FROM user WHERE id=? AND password=?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, id);
            pstmt.setString(2, pass);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return "登入成功！";
                } else
                    return "帳號或密碼錯誤！";
            }

        } catch (ClassNotFoundException e) {
            System.out.println("找不到 JDBC Driver：" + e.getMessage());

        } catch (SQLException e) {
            System.out.println("資料庫錯誤：" + e.getMessage());
        }
        return "發生錯誤！";
    }
}
