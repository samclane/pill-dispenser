package com.baseStationPackage;

import java.sql.*;


public class SQLiteJDBC{

    public static Connection connect() {
        Connection conn = null;
        try {
            String url = "jdbc:sqlite:C:/sqlite/PillvenDatabase.db";
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static Object select(String select, String from, String where, String is) {

        String sql = "SELECT " + select + " FROM " + from + " WHERE " + where + " IS " + "'" + is + "'";
        try (Connection conn = connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)){
            return rs.getObject(select);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static Object select(String select, String from) {

        String sql = "SELECT " + select + " FROM " + from;
        try (Connection conn = connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)){
            return rs.getObject(select);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static void main(String[] args) {

    }

}