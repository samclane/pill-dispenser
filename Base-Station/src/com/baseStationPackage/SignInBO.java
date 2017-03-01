package com.baseStationPackage;


import java.sql.SQLException;

public class SignInBO extends SignInDAO{


    public static boolean passwordCorrect(String userName, String password) throws SQLException {

        if(password.equals(getPassword(userName))) {
            return true;
        }
        else {
            return false;
        }

    }

}
