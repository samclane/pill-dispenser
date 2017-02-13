package com.baseStationPackage;


public class ViewPatientDataDAO extends SQLiteJDBC{



    public static Object getPatientResultSet() {

        String select =" * ";
        String from = " PATIENT_DATA ";
        return select(select, from);
    }

}
