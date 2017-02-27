package com.baseStationPackage;


import java.util.Vector;

public class ViewPatientDataBO extends ViewPatientDataDAO {

    public static Vector getLabelData() {
        return getColNamesFromDatabase("*","PATIENT_DATA");
    }

    public static Vector<Vector> getRowData(String searchBySelection, String searchByText) {
        return getRowDataFromDatabase("*","PATIENT_DATA", searchBySelection, searchByText);
    }

    public static void updateDatabase(Vector vector) {
        setRowDataToDatabase(vector);
    }

}
