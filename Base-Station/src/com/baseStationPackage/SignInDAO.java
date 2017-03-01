package com.baseStationPackage;


public class SignInDAO extends SQLiteJDBC{

    public static String getPassword(String userName) {

        String select ="PASSWORD";
        String from = "SIGN_IN";
        String where = "USER_NAME";
        Object password = null;
        password = select(select, from, where, userName);

        return password.toString();
    }

}
