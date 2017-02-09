package com.baseStationPackage;

import java.sql.*;

public class SignInDAO extends SQLiteJDBCDriverConnection{

    public static ResultSet getPassword(String userName) {
        //language=SQLite
        String sql = " SELECT PASSWORD FROM SIGN_IN WHERE USER_NAME IS 'Nurse' ";
        Connection conn = connect();
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ResultSet password = null;
        if (stmt != null) {
            try {
                password = stmt.executeQuery(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        return password;
    }

}
