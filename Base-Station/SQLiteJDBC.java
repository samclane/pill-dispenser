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

        String sql = buildSelectSQL(select, from, where, is);
        try (Connection conn = connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)){
            return rs.getObject(select);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static String buildSelectSQL(String select, String from, String where, String is) {
        return "SELECT " + select + " FROM " + from + " WHERE " + where + " IS " + "'" + is + "'";
    }

    public static String buildSelectSQL(String select, String from) {
        return "SELECT " + select + " FROM " + from;
    }

    public static String buildUpdateSQL(String table, String whereColumn, String whereValue, String moreSet) {
        return "UPDATE " + table +
                " SET " + moreSet +
                " WHERE " + whereColumn + "= '" + whereValue + "'";
    }

    //need to add commas in for loop in DAO
    public static String moreSetSQL(String setColumn, String setValue) {
        return setColumn + "= '" + setValue + "'";
    }



    public static void main(String[] args) {

    }

}