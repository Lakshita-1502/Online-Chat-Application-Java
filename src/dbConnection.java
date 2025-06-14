package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dbConnection {
    private static Connection con;

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        if (con == null) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/online_chat_application",
                    "root",
                    "MYsql@2005"
            );
        }
        return con;
    }
}