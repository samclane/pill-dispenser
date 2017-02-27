package com.baseStationPackage;


import java.sql.*;
import java.util.Vector;

public class ViewPatientDataDAO extends SQLiteJDBC{



    public static Vector getColNamesFromDatabase(String select, String from) {
        String sql = buildSelectSQL(select, from);
        Vector<String> dropDownNames = new Vector<>();
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            ResultSetMetaData cols = rs.getMetaData();
            for(int i = 1; i <= cols.getColumnCount(); i++) {
                dropDownNames.add(cols.getColumnName(i));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return dropDownNames;
    }


    public static Vector<Vector> getRowDataFromDatabase(String select, String from, String where, String is) {

        String sql = buildSelectSQL(select, from, where, is);
        Vector<Vector> rowData = new Vector<>();
        Vector row;
        try {
            Connection conn = connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();
            int colCount = meta.getColumnCount();
            row = new Vector(colCount);
            for (int i = 1; i <= colCount; i++) {
                row.add(meta.getColumnName(i));
            }
            rowData.add(row);
            while(rs.next()) {
                row = new Vector(colCount);
                for(int i = 1; i <= colCount; i++) {
                    row.add(rs.getString(i));
                }
                rowData.add(row);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return rowData;
    }

    public static void setRowDataToDatabase(Vector data) {
        Vector colNames = getColNamesFromDatabase("*", "PATIENT_DATA");
        String setValue = "";
        String setColumn = "";
        String whereColumn = "";
        String whereValue = "";
        String moreSet = "";
        whereValue = data.get(0).toString();//get Name value
        whereColumn = colNames.get(0).toString();//gets "NAME
        for(int i = 1; i<data.size(); i++) {//for loop skips 0 because its the name
            setColumn = colNames.get(i).toString();
            setValue = data.get(i).toString();
            moreSet = moreSet + moreSetSQL(setColumn,setValue);
            if(i!=(data.size()-1)) {
                moreSet = moreSet + ", ";
            }
        }
        String sql = buildUpdateSQL("PATIENT_DATA",whereColumn,whereValue,moreSet);
        try {
            Connection conn = connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }



}
