import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/payroll_db"; // Change if needed
    private static final String USER = "root";  // Your MySQL username
    private static final String PASSWORD = "your_password";  // Your MySQL password

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");  // Ensures MySQL Driver is loaded
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found", e);
        }
    }
}
